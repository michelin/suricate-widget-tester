package io.suricate.widget.tester.backend.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.suricate.widget.tester.backend.model.dto.nashorn.WidgetVariableResponse;
import io.suricate.widget.tester.backend.model.dto.widget.WidgetDto;
import io.suricate.widget.tester.backend.model.dto.widget.WidgetParamDto;
import io.suricate.widget.tester.backend.model.dto.widget.WidgetParamValueDto;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WidgetUtils {

    /**
     * Class logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetUtils.class);

    /**
     * Object mapper for jackson
     */
    private static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper(new YAMLFactory());
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Get widget from a given folder
     *
     * @param folder The folder from which to retrieve the widget
     * @return The built widget from the folder
     * @throws IOException Triggered exception during the widget files reading
     */
    public static WidgetDto getWidget(File folder) throws IOException {
        if (folder == null) {
            return null;
        }

        WidgetDto widget = new WidgetDto();
        List<File> files = FilesUtils.getFiles(folder);

        if (!files.isEmpty()) {
            for (File file : files) {
                readWidgetConfig(widget, file);
            }

            if (widget.getDelay() == null) {
                LOGGER.error("Widget delay must no be null : {}", folder.getPath());
                return null;
            }

            if (widget.getDelay() > 0 && StringUtils.isBlank(widget.getBackendJs())) {
                LOGGER.error("Widget script must not be empty when delay > 0 : {}", folder.getPath());
                return null;
            }

            if (StringUtils.isAnyBlank(widget.getCssContent(), widget.getDescription(), widget.getHtmlContent(), widget.getTechnicalName(), widget.getName())) {
                LOGGER.error("Widget is not well formatted : {}", folder.getPath());
                return null;
            }
        }

        return widget;
    }

    /**
     * Read the given file. According to the name of the file,
     * fill the widget with the information contained in the file
     *
     * @param widget The widget
     * @param file The file containing information to set to the widget
     * @throws IOException Exception triggered during file reading
     */
    private static void readWidgetConfig(WidgetDto widget, File file) throws IOException {
        if ("image".equals(FilenameUtils.getBaseName(file.getName()))) {
            widget.setImage(FileUtils.readFileToByteArray(file));
        } else if ("description".equals(FilenameUtils.getBaseName(file.getName()))) {
            mapper.readerForUpdating(widget).readValue(file);
        } else if ("script".equals(FilenameUtils.getBaseName(file.getName()))) {
            widget.setBackendJs(StringUtils.trimToNull(FileUtils.readFileToString(file, StandardCharsets.UTF_8)));
        } else if ("style".equals(FilenameUtils.getBaseName(file.getName()))) {
            widget.setCssContent(StringUtils.trimToNull(FileUtils.readFileToString(file, StandardCharsets.UTF_8)));
        } else if ("content".equals(FilenameUtils.getBaseName(file.getName()))) {
            widget.setHtmlContent(StringUtils.trimToNull(FileUtils.readFileToString(file, StandardCharsets.UTF_8)));
        } else if ("params".equals(FilenameUtils.getBaseName(file.getName()))) {
            mapper.readerForUpdating(widget).readValue(file);
        }
    }

    /**
     * Get the list of widget parameters
     *
     * @param widget The widget
     * @return The list of widget parameters
     */
    public static List<WidgetVariableResponse> getWidgetParametersForNashorn(final WidgetDto widget) {
        List<WidgetVariableResponse> widgetVariableResponses = new ArrayList<>();

        for (WidgetParamDto widgetParameter : widget.getWidgetParams()) {
            WidgetVariableResponse widgetVariableResponse = new WidgetVariableResponse();
            widgetVariableResponse.setName(widgetParameter.getName());
            widgetVariableResponse.setDescription(widgetParameter.getDescription());
            widgetVariableResponse.setType(widgetParameter.getType());
            widgetVariableResponse.setDefaultValue(widgetParameter.getDefaultValue());

            if (widgetVariableResponse.getType() != null) {
                switch (widgetVariableResponse.getType()) {
                    case COMBO:

                    case MULTIPLE:
                        widgetVariableResponse.setValues(getWidgetParamValuesAsMap(widgetParameter.getValues()));
                        break;

                    default:
                        widgetVariableResponse.setData(StringUtils.trimToNull(widgetParameter.getDefaultValue()));
                        break;
                }
            }

            widgetVariableResponses.add(widgetVariableResponse);
        }

        return widgetVariableResponses;
    }

    /**
     * Get the widget param list as a Map
     *
     * @param widgetParamValues The list of the widget param values
     * @return The list as a Map<String, String>
     */
    public static Map<String, String> getWidgetParamValuesAsMap(List<WidgetParamValueDto> widgetParamValues) {
        return widgetParamValues
                .stream()
                .collect(Collectors.toMap(WidgetParamValueDto::getJsKey, WidgetParamValueDto::getValue));
    }
}
