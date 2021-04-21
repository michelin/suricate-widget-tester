package io.suricate.widget.tester.backend;

import io.suricate.widget.tester.backend.model.dto.nashorn.NashornRequest;
import io.suricate.widget.tester.backend.model.dto.nashorn.NashornResponse;
import io.suricate.widget.tester.backend.model.dto.widget.WidgetDto;
import io.suricate.widget.tester.backend.properties.ApplicationProperties;
import io.suricate.widget.tester.backend.services.nashorn.tasks.NashornRequestWidgetExecutionAsyncTask;
import io.suricate.widget.tester.backend.utils.JsonUtils;
import io.suricate.widget.tester.backend.utils.WidgetUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Date;

@SpringBootApplication
public class WidgetTesterBackendApplication implements CommandLineRunner {

	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WidgetTesterBackendApplication.class);

	/**
	 * Application properties
	 */
	ApplicationProperties applicationProperties;

	/**
	 * Constructor
	 *
	 * @param applicationProperties The application properties to inject
	 */
	public WidgetTesterBackendApplication(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	/**
	 * Main method
	 *
	 * @param args Given input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(WidgetTesterBackendApplication.class, args);
	}

	/**
	 * Run method
	 *
	 * @param args Given input arguments
	 * @throws Exception Any triggered exception
	 */
	@Override
	public void run(String... args) throws Exception {
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
			WidgetTesterBackendApplication.LOGGER.info("----------- START LOG ---------");
			WidgetTesterBackendApplication.LOGGER.info(response.getLog());
			WidgetTesterBackendApplication.LOGGER.info("----------- END LOG ---------");
		}

		if (response.getError() != null) {
			WidgetTesterBackendApplication.LOGGER.info("----------- START ERROR ---------");
			WidgetTesterBackendApplication.LOGGER.info(response.getError().name());
			WidgetTesterBackendApplication.LOGGER.info("----------- END ERROR ---------");
		}

		LOGGER.info("------------ START RESPONSE --------");
		LOGGER.info("\n" + JsonUtils.prettifyJson(response.getData()));
		LOGGER.info("------------ END RESPONSE --------");
	}
}
