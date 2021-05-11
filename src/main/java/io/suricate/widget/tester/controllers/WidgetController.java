package io.suricate.widget.tester.controllers;

import io.suricate.widget.tester.services.api.WidgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping(value = "/v1/run")
    public void runWidget() throws IOException {
      this.widgetService.runWidget();
    }
}
