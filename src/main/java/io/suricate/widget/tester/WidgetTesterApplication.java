package io.suricate.widget.tester;

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
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Date;

@SpringBootApplication
public class WidgetTesterApplication implements CommandLineRunner {

	/**
	 * LOGGER
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(WidgetTesterApplication.class);

	/**
	 * Application properties
	 */
	ApplicationProperties applicationProperties;

	/**
	 * Constructor
	 *
	 * @param applicationProperties The application properties to inject
	 */
	public WidgetTesterApplication(ApplicationProperties applicationProperties) {
		this.applicationProperties = applicationProperties;
	}

	/**
	 * Main method
	 *
	 * @param args Given input arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(WidgetTesterApplication.class, args);
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
			WidgetTesterApplication.LOGGER.info("----------- START LOG ---------");
			WidgetTesterApplication.LOGGER.info(response.getLog());
			WidgetTesterApplication.LOGGER.info("----------- END LOG ---------");
		}

		if (response.getError() != null) {
			WidgetTesterApplication.LOGGER.info("----------- START ERROR ---------");
			WidgetTesterApplication.LOGGER.info(response.getError().name());
			WidgetTesterApplication.LOGGER.info("----------- END ERROR ---------");
		}

		LOGGER.info("------------ START RESPONSE --------");
		LOGGER.info("\n" + JsonUtils.prettifyJson(response.getData()));
		LOGGER.info("------------ END RESPONSE --------");
	}
}
