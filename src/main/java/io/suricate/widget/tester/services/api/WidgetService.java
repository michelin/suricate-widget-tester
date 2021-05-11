package io.suricate.widget.tester.services.api;

import io.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import io.suricate.widget.tester.model.dto.nashorn.NashornRequest;
import io.suricate.widget.tester.model.dto.nashorn.NashornResponse;
import io.suricate.widget.tester.model.dto.widget.WidgetDto;
import io.suricate.widget.tester.properties.ApplicationProperties;
import io.suricate.widget.tester.services.nashorn.tasks.NashornRequestWidgetExecutionAsyncTask;
import io.suricate.widget.tester.utils.JsonUtils;
import io.suricate.widget.tester.utils.WidgetUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Date;

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

        return nashornRequestWidgetExecutionAsyncTask.call();
    }
}
