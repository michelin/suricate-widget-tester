package io.suricate.widget.tester.controllers;

import io.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import io.suricate.widget.tester.model.dto.nashorn.NashornResponse;
import io.suricate.widget.tester.model.enums.ApiErrorEnum;
import io.suricate.widget.tester.services.api.WidgetService;
import io.suricate.widget.tester.utils.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * Widget controller
 */
@RestController
@RequestMapping("/api")
public class WidgetController {

    /**
     * Widget service
     */
    private final WidgetService widgetService;

    /**
     * Constructor
     *
     * @param widgetService The widget service
     */
    @Autowired
    public WidgetController(WidgetService widgetService) {
      this.widgetService = widgetService;
    }

    /**
     * Run the widget configured in the application properties
     */
    @PostMapping(value = "/v1/run")
    public void runWidget(@RequestBody WidgetExecutionRequestDto widgetExecutionRequestDto) throws IOException {
        NashornResponse nashornResponse = this.widgetService.runWidget(widgetExecutionRequestDto);

        if (nashornResponse.getError() != null) {
          throw new ApiException(nashornResponse.getLog(), ApiErrorEnum.INTERNAL_SERVER_ERROR);
        }


    }
}
