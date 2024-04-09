package com.michelin.suricate.widget.tester.service.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.MustacheFactory;
import com.michelin.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import com.michelin.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import com.michelin.suricate.widget.tester.model.dto.api.WidgetParametersRequestDto;
import com.michelin.suricate.widget.tester.model.dto.category.CategoryDto;
import com.michelin.suricate.widget.tester.model.dto.category.CategoryParameterDto;
import com.michelin.suricate.widget.tester.model.dto.js.WidgetVariableResponse;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetDto;
import com.michelin.suricate.widget.tester.property.ApplicationProperties;
import com.michelin.suricate.widget.tester.service.js.JsExecutionService;
import com.michelin.suricate.widget.tester.util.WidgetUtils;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class WidgetServiceTest {
    @Mock
    private MustacheFactory mustacheFactory;

    @Mock
    private JsExecutionService jsExecutionService;

    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private WidgetService widgetService;

    @NotNull
    private static WidgetExecutionRequestDto getWidgetExecutionRequestDto() {
        WidgetParametersRequestDto widgetParametersRequestDto = new WidgetParametersRequestDto();
        widgetParametersRequestDto.setName("SURI_TITLE");
        widgetParametersRequestDto.setValue("myTitle");

        WidgetParametersRequestDto breakLineWidgetParametersRequestDto = new WidgetParametersRequestDto();
        breakLineWidgetParametersRequestDto.setName("SURI_DESC");
        breakLineWidgetParametersRequestDto.setValue("desc\ndesc");

        WidgetExecutionRequestDto widgetExecutionRequestDto = new WidgetExecutionRequestDto();
        widgetExecutionRequestDto.setCategory("github");
        widgetExecutionRequestDto.setWidget("count-issues");
        widgetExecutionRequestDto.setPreviousData("previousData");
        widgetExecutionRequestDto.setParameters(
            Arrays.asList(widgetParametersRequestDto, breakLineWidgetParametersRequestDto));
        return widgetExecutionRequestDto;
    }

    @NotNull
    private static WidgetExecutionRequestDto getExecutionRequestDto() {
        WidgetParametersRequestDto widgetParametersRequestDto = new WidgetParametersRequestDto();
        widgetParametersRequestDto.setName("name");
        widgetParametersRequestDto.setValue("value");

        WidgetExecutionRequestDto widgetExecutionRequestDto = new WidgetExecutionRequestDto();
        widgetExecutionRequestDto.setCategory("github");
        widgetExecutionRequestDto.setWidget("count-issues");
        widgetExecutionRequestDto.setPreviousData("previousData");
        widgetExecutionRequestDto.setParameters(Collections.singletonList(widgetParametersRequestDto));
        return widgetExecutionRequestDto;
    }

    @Test
    void shouldGetWidget() throws IOException {
        try (MockedStatic<WidgetUtils> mocked = mockStatic(WidgetUtils.class)) {
            when(applicationProperties.getWidgets())
                .thenReturn(new ApplicationProperties.Widgets());

            WidgetDto widgetDto = new WidgetDto();
            widgetDto.setId(1L);
            widgetDto.setName("name");

            CategoryParameterDto categoryParameterDto = new CategoryParameterDto();
            categoryParameterDto.setKey("key");
            categoryParameterDto.setValue("value");

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(1L);
            categoryDto.setConfigurations(Collections.singleton(categoryParameterDto));

            mocked.when(() -> WidgetUtils.getWidget(any(), any()))
                .thenReturn(widgetDto);
            mocked.when(() -> WidgetUtils.getCategory(any(), any()))
                .thenReturn(categoryDto);

            WidgetDto actual =
                widgetService.getWidget("category", "widget");

            assertEquals(1L, actual.getId());
            assertEquals("name", actual.getName());
            assertEquals("key", actual.getWidgetParams().get(0).getName());
        }
    }

    @Test
    void shouldRunWidget() throws IOException {
        try (MockedStatic<WidgetUtils> mocked = mockStatic(WidgetUtils.class)) {
            WidgetDto widgetDto = new WidgetDto();
            widgetDto.setId(1L);
            widgetDto.setName("name");
            widgetDto.setTechnicalName("technicalName");
            widgetDto.setBackendJs("function run () { print('title='+SURI_TITLE); return '{\"data\": \"test\"}'; }");
            widgetDto.setCssContent("cssContent");
            widgetDto.setDelay(10L);
            widgetDto.setHtmlContent("<h1>{{data}}</h1><h1>{{SURI_DESC}}</h1>");

            CategoryParameterDto categoryParameterDto = new CategoryParameterDto();
            categoryParameterDto.setKey("key");
            categoryParameterDto.setValue("value");

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(1L);
            categoryDto.setConfigurations(Collections.singleton(categoryParameterDto));

            WidgetVariableResponse widgetVariableResponse = new WidgetVariableResponse();
            widgetVariableResponse.setName("name");

            mocked.when(() -> WidgetUtils.getWidget(any(), any()))
                .thenReturn(widgetDto);
            mocked.when(() -> WidgetUtils.getCategory(any(), any()))
                .thenReturn(categoryDto);
            mocked.when(() -> WidgetUtils.getWidgetParametersForJsExecution(any()))
                .thenReturn(Collections.singletonList(widgetVariableResponse));
            when(applicationProperties.getWidgets())
                .thenReturn(new ApplicationProperties.Widgets());
            when(jsExecutionService.isJsExecutable(any()))
                .thenReturn(true);
            when(mustacheFactory.compile(any(), any()))
                .thenReturn(new DefaultMustacheFactory().compile(new StringReader(widgetDto.getHtmlContent()),
                    widgetDto.getTechnicalName()));

            WidgetExecutionRequestDto widgetExecutionRequestDto =
                getWidgetExecutionRequestDto();

            ProjectWidgetResponseDto actual = widgetService.runWidget(widgetExecutionRequestDto);

            assertEquals("<h1>test</h1><h1>desc&#10;desc</h1>", actual.getInstantiateHtml());
            assertEquals("technicalName", actual.getTechnicalName());
            assertEquals("cssContent", actual.getCssContent());
        }
    }

    @Test
    void shouldNotRunWidgetWhenRequestNotExecutable() throws IOException {
        try (MockedStatic<WidgetUtils> mocked = mockStatic(WidgetUtils.class)) {
            WidgetDto widgetDto = new WidgetDto();
            widgetDto.setId(1L);
            widgetDto.setName("name");
            widgetDto.setTechnicalName("technicalName");
            widgetDto.setBackendJs("backendJs");
            widgetDto.setCssContent("cssContent");
            widgetDto.setDelay(10L);
            widgetDto.setHtmlContent("<h1>{{data}}</h1>");
            widgetDto.setLibraries(new String[] {"lib"});

            CategoryParameterDto categoryParameterDto = new CategoryParameterDto();
            categoryParameterDto.setKey("key");
            categoryParameterDto.setValue("value");

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(1L);
            categoryDto.setConfigurations(Collections.singleton(categoryParameterDto));

            mocked.when(() -> WidgetUtils.getWidget(any(), any()))
                .thenReturn(widgetDto);
            mocked.when(() -> WidgetUtils.getCategory(any(), any()))
                .thenReturn(categoryDto);
            when(applicationProperties.getWidgets())
                .thenReturn(new ApplicationProperties.Widgets());
            when(jsExecutionService.isJsExecutable(any()))
                .thenReturn(false);
            when(mustacheFactory.compile(any(), any()))
                .thenReturn(new DefaultMustacheFactory().compile(new StringReader(widgetDto.getHtmlContent()),
                    widgetDto.getTechnicalName()));

            WidgetExecutionRequestDto widgetExecutionRequestDto = getExecutionRequestDto();

            ProjectWidgetResponseDto actual = widgetService.runWidget(widgetExecutionRequestDto);

            assertEquals("<h1></h1>", actual.getInstantiateHtml());
            assertEquals("technicalName", actual.getTechnicalName());
            assertEquals("cssContent", actual.getCssContent());
            assertEquals("lib", actual.getLibrariesNames().get(0));
        }
    }

    @Test
    void shouldNotRunWidgetWhenWrongBackendJs() throws IOException {
        try (MockedStatic<WidgetUtils> mocked = mockStatic(WidgetUtils.class)) {
            WidgetDto widgetDto = new WidgetDto();
            widgetDto.setId(1L);
            widgetDto.setName("name");
            widgetDto.setTechnicalName("technicalName");
            widgetDto.setBackendJs("backendJs");
            widgetDto.setCssContent("cssContent");
            widgetDto.setDelay(10L);
            widgetDto.setHtmlContent("<h1>{{data}}</h1>");
            widgetDto.setLibraries(new String[] {"lib"});

            CategoryParameterDto categoryParameterDto = new CategoryParameterDto();
            categoryParameterDto.setKey("key");
            categoryParameterDto.setValue("value");

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(1L);
            categoryDto.setConfigurations(Collections.singleton(categoryParameterDto));

            mocked.when(() -> WidgetUtils.getWidget(any(), any()))
                .thenReturn(widgetDto);
            mocked.when(() -> WidgetUtils.getCategory(any(), any()))
                .thenReturn(categoryDto);
            when(applicationProperties.getWidgets())
                .thenReturn(new ApplicationProperties.Widgets());
            when(jsExecutionService.isJsExecutable(any()))
                .thenReturn(true);

            WidgetExecutionRequestDto widgetExecutionRequestDto = getExecutionRequestDto();

            ProjectWidgetResponseDto actual = widgetService.runWidget(widgetExecutionRequestDto);

            assertEquals("ReferenceError: backendJs is not defined", actual.getLog());
        }
    }

    @Test
    void shouldInstantiateProjectWidgetHtmlNoData() {
        WidgetDto widgetDto = new WidgetDto();
        widgetDto.setId(1L);
        widgetDto.setHtmlContent("<h1>Titre</h1>");

        String actual = widgetService.instantiateProjectWidgetHtml(widgetDto, "", "param=value");

        assertEquals("<h1>Titre</h1>", actual);
    }

    @Test
    void shouldInstantiateProjectWidgetHtml() {
        WidgetDto widgetDto = new WidgetDto();
        widgetDto.setId(1L);
        widgetDto.setHtmlContent("<h1>{{DATA}}</h1>");

        when(mustacheFactory.compile(any(), any()))
            .thenReturn(new DefaultMustacheFactory().compile(new StringReader(widgetDto.getHtmlContent()),
                widgetDto.getTechnicalName()));

        String actual = widgetService.instantiateProjectWidgetHtml(widgetDto, "{\"DATA\": \"titre\"}", "param=value");

        assertEquals("<h1>titre</h1>", actual);
    }

    @Test
    void shouldThrowMustacheExceptionWhenInstantiateProjectWidgetHtml() {
        WidgetDto widgetDto = new WidgetDto();
        widgetDto.setId(1L);
        widgetDto.setHtmlContent("<h1>{{DATA}}</h1>");

        when(mustacheFactory.compile(any(), any()))
            .thenThrow(new MustacheException("Error"));

        String actual = widgetService.instantiateProjectWidgetHtml(widgetDto, "{\"DATA\": \"titre\"}", "param=value");

        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldThrowExceptionFromDataWhenInstantiateProjectWidgetHtml() {
        WidgetDto widgetDto = new WidgetDto();
        widgetDto.setId(1L);
        widgetDto.setTechnicalName("technicalName");
        widgetDto.setHtmlContent("<h1>{{DATA}}</h1>");

        when(mustacheFactory.compile(any(), any()))
            .thenReturn(new DefaultMustacheFactory().compile(new StringReader(widgetDto.getHtmlContent()),
                widgetDto.getTechnicalName()));

        String actual = widgetService.instantiateProjectWidgetHtml(widgetDto, "parseError", "param=value");

        assertEquals("<h1></h1>", actual);
    }
}
