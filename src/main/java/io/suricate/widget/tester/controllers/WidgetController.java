package io.suricate.widget.tester.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import io.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import io.suricate.widget.tester.model.dto.nashorn.NashornResponse;
import io.suricate.widget.tester.model.dto.widget.WidgetDto;
import io.suricate.widget.tester.model.dto.widget.WidgetParamDto;
import io.suricate.widget.tester.model.enums.ApiErrorEnum;
import io.suricate.widget.tester.services.api.WidgetService;
import io.suricate.widget.tester.utils.JavaScriptUtils;
import io.suricate.widget.tester.utils.PropertiesUtils;
import io.suricate.widget.tester.utils.exceptions.ApiException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

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
    public ResponseEntity<ProjectWidgetResponseDto> runWidget(@RequestBody WidgetExecutionRequestDto widgetExecutionRequestDto) throws IOException {
        ProjectWidgetResponseDto projectWidgetResponseDto = this.widgetService.runWidget(widgetExecutionRequestDto);

        if (projectWidgetResponseDto.getLog() != null) {
          throw new ApiException(projectWidgetResponseDto.getLog(), ApiErrorEnum.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
          .ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(projectWidgetResponseDto);
    }
}
