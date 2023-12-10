package com.michelin.suricate.widget.tester.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.michelin.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import com.michelin.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import com.michelin.suricate.widget.tester.services.api.WidgetService;
import com.michelin.suricate.widget.tester.utils.exceptions.ApiException;
import java.io.IOException;
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
    void shouldRunWidget() throws IOException {
        WidgetExecutionRequestDto widgetExecutionRequestDto = new WidgetExecutionRequestDto();
        widgetExecutionRequestDto.setPath("path");

        ProjectWidgetResponseDto projectWidgetResponseDto = new ProjectWidgetResponseDto();
        projectWidgetResponseDto.setTechnicalName("technicalName");

        when(widgetService.runWidget(any()))
            .thenReturn(projectWidgetResponseDto);

        ResponseEntity<ProjectWidgetResponseDto> actual = widgetController.runWidget(widgetExecutionRequestDto);

        assertThat(actual.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actual.getBody()).isEqualTo(projectWidgetResponseDto);
    }

    @Test
    void shouldRunWidgetAndThrowExceptionWhenErrorLogs() throws IOException {
        WidgetExecutionRequestDto widgetExecutionRequestDto = new WidgetExecutionRequestDto();
        widgetExecutionRequestDto.setPath("path");

        ProjectWidgetResponseDto projectWidgetResponseDto = new ProjectWidgetResponseDto();
        projectWidgetResponseDto.setLog("Error");

        when(widgetService.runWidget(any()))
            .thenReturn(projectWidgetResponseDto);

        assertThatThrownBy(() -> widgetController.runWidget(widgetExecutionRequestDto))
            .isInstanceOf(ApiException.class)
            .hasMessage("Error");
    }
}
