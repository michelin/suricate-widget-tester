package io.suricate.widget.tester.services.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.MustacheFactory;
import io.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import io.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import io.suricate.widget.tester.model.dto.nashorn.NashornRequest;
import io.suricate.widget.tester.model.dto.nashorn.NashornResponse;
import io.suricate.widget.tester.model.dto.widget.WidgetDto;
import io.suricate.widget.tester.model.dto.widget.WidgetParamDto;
import io.suricate.widget.tester.properties.ApplicationProperties;
import io.suricate.widget.tester.services.nashorn.tasks.NashornRequestWidgetExecutionAsyncTask;
import io.suricate.widget.tester.utils.JavaScriptUtils;
import io.suricate.widget.tester.utils.JsonUtils;
import io.suricate.widget.tester.utils.PropertiesUtils;
import io.suricate.widget.tester.utils.WidgetUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

/**
 * Widget service
 */
@Service
public class WidgetService {

    /**
     * LOGGER
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetService.class);

    /**
     * The mustacheFactory
     */
    private final MustacheFactory mustacheFactory;

    /**
     * Constructor
     *
     * @param mustacheFactory The mustache factory (HTML template)
     */
    @Autowired
    public WidgetService(MustacheFactory mustacheFactory) {
      this.mustacheFactory = mustacheFactory;
    }

    /**
     * Run the widget configured in the application properties
     */
    public NashornResponse runWidget(WidgetExecutionRequestDto widgetExecutionRequestDto) throws IOException {
        WidgetDto widget = WidgetUtils.getWidget(new File(widgetExecutionRequestDto.getPath()));

        StringBuilder propertiesBuilder = new StringBuilder();

      widgetExecutionRequestDto.getParameters().forEach(parameter -> propertiesBuilder
          .append(parameter.getName())
          .append("=")
          .append(parameter.getValue())
          .append("\n"));

        NashornRequest nashornRequest = new NashornRequest(propertiesBuilder.toString(), widget.getBackendJs(),
            widgetExecutionRequestDto.getPreviousData(), widget.getDelay(), 1L, 1L, new Date()
        );

        NashornRequestWidgetExecutionAsyncTask nashornRequestWidgetExecutionAsyncTask = new NashornRequestWidgetExecutionAsyncTask(nashornRequest,
          WidgetUtils.getWidgetParametersForNashorn(widget));

        NashornResponse nashornResponse = nashornRequestWidgetExecutionAsyncTask.call();

        // Failure
        if (nashornResponse.getError() != null) {
            return nashornResponse;
        }

        // Success
        ProjectWidgetResponseDto projectWidgetResponseDto = new ProjectWidgetResponseDto();
        projectWidgetResponseDto.setInstantiateHtml(this.instantiateProjectWidgetHtml(widget, nashornResponse.getData(), propertiesBuilder.toString()));
        projectWidgetResponseDto.setTechnicalName(widget.getTechnicalName());
        projectWidgetResponseDto.setCssContent(widget.getCssContent());

        nashornResponse.setProjectWidget(projectWidgetResponseDto);

        return nashornResponse;
    }

    /**
     * Instantiate the HTML of a widget with the data resulting from
     * the Nashorn execution.
     *
     * @param widget The widget
     * @param data The computed data
     * @param backendConfig The widget configuration
     * @return The instantiated HTML
     */
    public String instantiateProjectWidgetHtml(WidgetDto widget, String data, String backendConfig) {
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Object> map = null;

      String instantiateHtml = widget.getHtmlContent();
      if (StringUtils.isNotEmpty(data)) {
        try {
          map = objectMapper.readValue(data,
            new TypeReference<Map<String, Object>>() {
            }
          );

          // Add backend config
          map.putAll(PropertiesUtils.convertStringWidgetPropertiesToMap(backendConfig));
          map.put(JavaScriptUtils.WIDGET_INSTANCE_ID_VARIABLE, 1L);

          // Add global variables if needed
          for (WidgetParamDto widgetParam : widget.getWidgetParams()) {
            if (!map.containsKey(widgetParam.getName()) && widgetParam.isRequired()) {
              map.put(widgetParam.getName(), widgetParam.getDefaultValue());
            }
          }
        } catch (IOException e) {
          LOGGER.error(e.getMessage(), e);
        }

        StringWriter stringWriter = new StringWriter();
        try {
          Mustache mustache = mustacheFactory.compile(new StringReader(instantiateHtml), widget.getTechnicalName());
          mustache.execute(stringWriter, map);
        } catch (MustacheException me) {
          LOGGER.error("Error with mustache template for widget {}", widget.getTechnicalName(), me);
        }
        stringWriter.flush();
        instantiateHtml = stringWriter.toString();
      }

      return instantiateHtml;
    }
}
