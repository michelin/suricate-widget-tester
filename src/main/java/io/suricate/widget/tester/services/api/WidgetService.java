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
import io.suricate.widget.tester.services.nashorn.services.NashornService;
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
     * The nashorn service
     */
    private NashornService nashornService;

    /**
     * Constructor
     *
     * @param mustacheFactory The mustache factory (HTML template)
     * @param nashornService The nashorn service
     */
    @Autowired
    public WidgetService(MustacheFactory mustacheFactory,
                         NashornService nashornService) {
      this.mustacheFactory = mustacheFactory;
      this.nashornService = nashornService;
    }

    /**
     * Run the widget configured in the application properties
     */
    public ProjectWidgetResponseDto runWidget(WidgetExecutionRequestDto widgetExecutionRequestDto) throws IOException {
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

        ProjectWidgetResponseDto projectWidgetResponseDto = new ProjectWidgetResponseDto();
        projectWidgetResponseDto.setTechnicalName(widget.getTechnicalName());
        projectWidgetResponseDto.setCssContent(widget.getCssContent());

        String data = StringUtils.EMPTY;

        if (this.nashornService.isNashornRequestExecutable(nashornRequest)) {
            NashornRequestWidgetExecutionAsyncTask nashornRequestWidgetExecutionAsyncTask = new NashornRequestWidgetExecutionAsyncTask(nashornRequest,
              WidgetUtils.getWidgetParametersForNashorn(widget));

            NashornResponse nashornResponse = nashornRequestWidgetExecutionAsyncTask.call();

            // Failure
            if (nashornResponse.getError() != null) {
                projectWidgetResponseDto.setLog(nashornResponse.getLog());
                return projectWidgetResponseDto;
            }

            data = nashornResponse.getData();
        }

        // Success
        projectWidgetResponseDto.setInstantiateHtml(this.instantiateProjectWidgetHtml(widget, data, propertiesBuilder.toString()));

        return projectWidgetResponseDto;
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
