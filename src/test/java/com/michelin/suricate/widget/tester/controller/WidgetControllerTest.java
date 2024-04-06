package com.michelin.suricate.widget.tester.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.michelin.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import com.michelin.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetParamDto;
import com.michelin.suricate.widget.tester.service.api.WidgetService;
import com.michelin.suricate.widget.tester.util.exception.ApiException;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class WidgetControllerTest {
    @Mock
    private WidgetService widgetService;

    @InjectMocks
    private WidgetController widgetController;

    @Test
    void shouldGetWidgetParameters() throws IOException {
        WidgetDto widgetDto = new WidgetDto();
        widgetDto.setWidgetParams(List.of(new WidgetParamDto()));

        when(widgetService.getWidget("category", "widget"))
            .thenReturn(widgetDto);

        ResponseEntity<List<WidgetParamDto>> actual = widgetController.getWidgetParameters("category", "widget");

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(widgetDto.getWidgetParams(), actual.getBody());
    }

    @Test
    void shouldRunWidget() throws IOException {
        WidgetExecutionRequestDto widgetExecutionRequestDto = new WidgetExecutionRequestDto();
        widgetExecutionRequestDto.setCategory("category");
        widgetExecutionRequestDto.setWidget("widget");

        ProjectWidgetResponseDto projectWidgetResponseDto = new ProjectWidgetResponseDto();
        projectWidgetResponseDto.setTechnicalName("technicalName");

        when(widgetService.runWidget(any()))
            .thenReturn(projectWidgetResponseDto);

        ResponseEntity<ProjectWidgetResponseDto> actual = widgetController.runWidget(widgetExecutionRequestDto);

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertEquals(projectWidgetResponseDto, actual.getBody());
    }

    @Test
    void shouldRunWidgetAndThrowExceptionWhenErrorLogs() throws IOException {
        WidgetExecutionRequestDto widgetExecutionRequestDto = new WidgetExecutionRequestDto();
        widgetExecutionRequestDto.setCategory("category");
        widgetExecutionRequestDto.setWidget("widget");

        ProjectWidgetResponseDto projectWidgetResponseDto = new ProjectWidgetResponseDto();
        projectWidgetResponseDto.setLog("Error");

        when(widgetService.runWidget(any()))
            .thenReturn(projectWidgetResponseDto);

        ApiException actual = assertThrows(ApiException.class, () -> widgetController.runWidget(widgetExecutionRequestDto));

        assertEquals("Error", actual.getMessage());
    }
}
