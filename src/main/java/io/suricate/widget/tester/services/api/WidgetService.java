package io.suricate.widget.tester.services.api;

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
     * Application properties
     */
    ApplicationProperties applicationProperties;

    /**
     * Constructor
     *
     * @param applicationProperties The application properties
     */
    @Autowired
    public WidgetService(ApplicationProperties applicationProperties) {
      this.applicationProperties = applicationProperties;
    }

    /**
     * Run the widget configured in the application properties
     */
    public void runWidget() throws IOException {
        WidgetDto widget = WidgetUtils.getWidget(new File(this.applicationProperties.getFolder()));

        StringBuilder propertiesBuilder = new StringBuilder();

        this.applicationProperties.getParameters().forEach((key, value) -> propertiesBuilder
          .append(key)
          .append("=")
          .append(value)
          .append("\n"));

        NashornRequest nashornRequest = new NashornRequest(propertiesBuilder.toString(), widget.getBackendJs(),
          this.applicationProperties.getPreviousData(), widget.getDelay(), 1L, 1L, new Date()
        );

        NashornRequestWidgetExecutionAsyncTask nashornRequestWidgetExecutionAsyncTask = new NashornRequestWidgetExecutionAsyncTask(nashornRequest,
          WidgetUtils.getWidgetParametersForNashorn(widget));

        NashornResponse response = nashornRequestWidgetExecutionAsyncTask.call();

        if (StringUtils.isNotBlank(response.getLog())) {
            WidgetService.LOGGER.info("----------- START LOG ---------");
            WidgetService.LOGGER.info(response.getLog());
            WidgetService.LOGGER.info("----------- END LOG ---------");
        }

        WidgetService.LOGGER.info("----------- START ERROR ---------");
        WidgetService.LOGGER.info("{}", response.getError());
        WidgetService.LOGGER.info("----------- END ERROR ---------");

        WidgetService.LOGGER.info("----------- START ERROR ---------");
        WidgetService.LOGGER.info("{}", JsonUtils.prettifyJson(response.getData()));
        WidgetService.LOGGER.info("------------ END RESPONSE --------");
    }
}
