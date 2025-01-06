package com.michelin.suricate.widget.tester.service.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.MustacheFactory;
import com.michelin.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import com.michelin.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import com.michelin.suricate.widget.tester.model.dto.category.CategoryDto;
import com.michelin.suricate.widget.tester.model.dto.js.JsExecutionDto;
import com.michelin.suricate.widget.tester.model.dto.js.JsResultDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetParamDto;
import com.michelin.suricate.widget.tester.property.ApplicationProperties;
import com.michelin.suricate.widget.tester.service.js.JsExecutionService;
import com.michelin.suricate.widget.tester.service.js.task.JsExecutionAsyncTask;
import com.michelin.suricate.widget.tester.util.JavaScriptUtils;
import com.michelin.suricate.widget.tester.util.JsonUtils;
import com.michelin.suricate.widget.tester.util.PropertiesUtils;
import com.michelin.suricate.widget.tester.util.WidgetUtils;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Widget service.
 */
@Slf4j
@Service
public class WidgetService {
    @Autowired
    private MustacheFactory mustacheFactory;

    @Autowired
    private JsExecutionService jsExecutionService;

    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * Get the widget according to the given widget path.
     * Load the category parameters as widget parameters
     *
     * @param category The category
     * @param widget   The widget
     * @return The widget
     */
    public WidgetDto getWidget(String category, String widget) throws IOException {
        String rootFolder = applicationProperties.getWidgets().getRepository();
        String categoryFolder = rootFolder + "/content/" + category;

        CategoryDto categoryDto = WidgetUtils.getCategory(rootFolder, new File(categoryFolder));
        WidgetDto widgetDto = WidgetUtils.getWidget(rootFolder, new File(categoryFolder + "/widgets/" + widget));

        categoryDto.getConfigurations().forEach(categoryParameterDto -> {
            WidgetParamDto widgetParamDto = new WidgetParamDto();
            widgetParamDto.setName(categoryParameterDto.getKey());
            widgetParamDto.setType(categoryParameterDto.getDataType());

            widgetDto.getWidgetParams().add(widgetParamDto);
        });

        return widgetDto;
    }

    /**
     * Run the widget configured in the application properties.
     *
     * @param widgetExecutionRequestDto The widget execution request
     * @return The widget response
     * @throws IOException When an error occurred during the widget execution
     */
    public ProjectWidgetResponseDto runWidget(WidgetExecutionRequestDto widgetExecutionRequestDto) throws IOException {
        WidgetDto widget = getWidget(widgetExecutionRequestDto.getCategory(), widgetExecutionRequestDto.getWidget());

        StringBuilder propertiesBuilder = new StringBuilder();

        widgetExecutionRequestDto.getParameters().forEach(parameter -> propertiesBuilder
            .append(parameter.getName())
            .append("=")
            .append(parameter.getValue().replace("\n", "\\n"))
            .append("\n"));

        ProjectWidgetResponseDto projectWidgetResponseDto = new ProjectWidgetResponseDto();
        projectWidgetResponseDto.setId(1L);
        projectWidgetResponseDto.setTechnicalName(widget.getTechnicalName());
        projectWidgetResponseDto.setCssContent(widget.getCssContent());

        if (widget.getLibraries() != null && widget.getLibraries().length > 0) {
            projectWidgetResponseDto.setLibrariesNames(Arrays.asList(widget.getLibraries()));
        }

        String data = "{}";

        JsExecutionDto jsExecutionDto = new JsExecutionDto(
            propertiesBuilder.toString(),
            widget.getBackendJs(),
            widgetExecutionRequestDto.getPreviousData(),
            1L,
            1L,
            widget.getDelay(), new Date()
        );

        if (jsExecutionService.isJsExecutable(jsExecutionDto)) {
            JsExecutionAsyncTask jsExecutionAsyncTask = new JsExecutionAsyncTask(
                jsExecutionDto,
                WidgetUtils.getWidgetParametersForJsExecution(widget)
            );

            JsResultDto jsResultDto = jsExecutionAsyncTask.call();

            logResponse(jsResultDto);

            // Failure
            if (jsResultDto.getError() != null) {
                projectWidgetResponseDto.setLog(jsResultDto.getLog());
                return projectWidgetResponseDto;
            }

            data = jsResultDto.getData();
        }

        // Success
        projectWidgetResponseDto.setInstantiateHtml(
            instantiateProjectWidgetHtml(widget, data, propertiesBuilder.toString())
        );

        return projectWidgetResponseDto;
    }

    /**
     * Instantiate the HTML of a widget with the data resulting from
     * the Js execution.
     *
     * @param widget        The widget
     * @param data          The computed data
     * @param backendConfig The widget configuration
     * @return The instantiated HTML
     */
    public String instantiateProjectWidgetHtml(WidgetDto widget, String data, String backendConfig) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = null;

        String instantiateHtml = widget.getHtmlContent();
        if (StringUtils.isNotEmpty(data)) {
            try {
                map = objectMapper.readValue(data, new TypeReference<>() {});

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
                Mustache mustache = mustacheFactory.compile(
                    new StringReader(instantiateHtml),
                    widget.getTechnicalName()
                );
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
     * Log information about the Js execution.
     *
     * @param jsResultDto The Js execution result
     */
    private void logResponse(JsResultDto jsResultDto) {
        if (StringUtils.isNotBlank(jsResultDto.getLog())) {
            log.info("----------- START LOG ---------");
            log.info(jsResultDto.getLog());
            log.info("----------- END LOG ---------");
        }

        if (StringUtils.isNotBlank(jsResultDto.getData())) {
            log.info("------------ START DATA --------");
            log.info(JsonUtils.prettifyJson(jsResultDto.getData()));
            log.info("------------ END DATA --------");
        }
    }
}
