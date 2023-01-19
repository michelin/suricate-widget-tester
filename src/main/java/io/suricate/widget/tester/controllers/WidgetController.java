package io.suricate.widget.tester.controllers;

import io.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import io.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import io.suricate.widget.tester.model.dto.widget.WidgetDto;
import io.suricate.widget.tester.model.dto.widget.WidgetParamDto;
import io.suricate.widget.tester.model.enums.ApiErrorEnum;
import io.suricate.widget.tester.services.api.WidgetService;
import io.suricate.widget.tester.utils.exceptions.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class WidgetController {
    @Autowired
    private WidgetService widgetService;

    /**
     * Get the widget parameters according to a given path
     */
    @GetMapping(value = "/v1/parameters")
    public ResponseEntity<List<WidgetParamDto>> getWidgetParameters(@RequestParam String widgetPath) throws IOException {
        WidgetDto widgetDto = widgetService.getWidget(widgetPath);

        return ResponseEntity
          .ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(widgetDto.getWidgetParams());
    }

    /**
     * Run the widget configured in the application properties
     */
    @PostMapping(value = "/v1/run")
    public ResponseEntity<ProjectWidgetResponseDto> runWidget(@RequestBody WidgetExecutionRequestDto widgetExecutionRequestDto) throws IOException {
        ProjectWidgetResponseDto projectWidgetResponseDto = widgetService.runWidget(widgetExecutionRequestDto);

        if (projectWidgetResponseDto.getLog() != null) {
          throw new ApiException(projectWidgetResponseDto.getLog(), ApiErrorEnum.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
          .ok()
          .contentType(MediaType.APPLICATION_JSON)
          .body(projectWidgetResponseDto);
    }
}
