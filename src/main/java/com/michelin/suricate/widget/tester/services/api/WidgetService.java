package com.michelin.suricate.widget.tester.services.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.MustacheFactory;
import com.michelin.suricate.widget.tester.model.dto.category.CategoryDto;
import com.michelin.suricate.widget.tester.services.nashorn.services.NashornService;
import com.michelin.suricate.widget.tester.services.nashorn.tasks.NashornRequestWidgetExecutionAsyncTask;
import com.michelin.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import com.michelin.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import com.michelin.suricate.widget.tester.model.dto.nashorn.NashornRequest;
import com.michelin.suricate.widget.tester.model.dto.nashorn.NashornResponse;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetParamDto;
import com.michelin.suricate.widget.tester.utils.JavaScriptUtils;
import com.michelin.suricate.widget.tester.utils.JsonUtils;
import com.michelin.suricate.widget.tester.utils.PropertiesUtils;
import com.michelin.suricate.widget.tester.utils.WidgetUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@Slf4j
@Service
public class WidgetService {
    @Autowired
    private MustacheFactory mustacheFactory;

    @Autowired
    private NashornService nashornService;

    /**
     * Get the widget according to the given widget path
     * Load the category parameters as widget parameters
     * @param widgetPath The widget path
     * @return The widget
     */
    public WidgetDto getWidget(String widgetPath) throws IOException {
        WidgetDto widgetDto = WidgetUtils.getWidget(new File(widgetPath));

        CategoryDto categoryDto = WidgetUtils.getCategory(new File(widgetPath)
                .getParentFile().getParentFile());

        categoryDto.getConfigurations().forEach(categoryParameterDto -> {
            WidgetParamDto widgetParamDto = new WidgetParamDto();
            widgetParamDto.setName(categoryParameterDto.getKey());

            widgetDto.getWidgetParams().add(widgetParamDto);
        });

        return widgetDto;
    }

    /**
     * Run the widget configured in the application properties
     */
    public ProjectWidgetResponseDto runWidget(WidgetExecutionRequestDto widgetExecutionRequestDto) throws IOException {
        WidgetDto widget = getWidget(widgetExecutionRequestDto.getPath());

        StringBuilder propertiesBuilder = new StringBuilder();

        widgetExecutionRequestDto.getParameters().forEach(parameter -> propertiesBuilder
            .append(parameter.getName())
            .append("=")
            .append(parameter.getValue())
            .append("\n"));

        NashornRequest nashornRequest = new NashornRequest(propertiesBuilder.toString(), widget.getBackendJs(),
            widgetExecutionRequestDto.getPreviousData(), 1L, 1L, widget.getDelay(), new Date());

        ProjectWidgetResponseDto projectWidgetResponseDto = new ProjectWidgetResponseDto();
        projectWidgetResponseDto.setTechnicalName(widget.getTechnicalName());
        projectWidgetResponseDto.setCssContent(widget.getCssContent());

        if (widget.getLibraries() != null && widget.getLibraries().length > 0) {
          projectWidgetResponseDto.setLibrariesNames(Arrays.asList(widget.getLibraries()));
        }

        String data = "{}";

        if (nashornService.isNashornRequestExecutable(nashornRequest)) {
            NashornRequestWidgetExecutionAsyncTask nashornRequestWidgetExecutionAsyncTask = new NashornRequestWidgetExecutionAsyncTask(nashornRequest,
              WidgetUtils.getWidgetParametersForNashorn(widget));

            NashornResponse nashornResponse = nashornRequestWidgetExecutionAsyncTask.call();

            logResponse(nashornResponse);

            // Failure
            if (nashornResponse.getError() != null) {
                projectWidgetResponseDto.setLog(nashornResponse.getLog());
                return projectWidgetResponseDto;
            }

            data = nashornResponse.getData();
        }

        // Success
        projectWidgetResponseDto.setInstantiateHtml(instantiateProjectWidgetHtml(widget, data, propertiesBuilder.toString()));

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
          log.error(e.getMessage(), e);
        }

        StringWriter stringWriter = new StringWriter();
        try {
          Mustache mustache = mustacheFactory.compile(new StringReader(instantiateHtml), widget.getTechnicalName());
          mustache.execute(stringWriter, map);
        } catch (MustacheException me) {
            log.error("Error with mustache template for widget {}", widget.getTechnicalName(), me);
        }
        stringWriter.flush();
        instantiateHtml = stringWriter.toString();
      }

      return instantiateHtml;
    }

    /**
     * Log information about the Nashorn response
     *
     * @param nashornResponse The Nashorn response
     */
    private void logResponse(NashornResponse nashornResponse) {
        if (StringUtils.isNotBlank(nashornResponse.getLog())) {
            log.info("----------- START LOG ---------");
            log.info(nashornResponse.getLog());
            log.info("----------- END LOG ---------");
        }

        if (StringUtils.isNotBlank(nashornResponse.getData())) {
            log.info("------------ START DATA --------");
            log.info(JsonUtils.prettifyJson(nashornResponse.getData()));
            log.info("------------ END DATA --------");
        }
    }
}
