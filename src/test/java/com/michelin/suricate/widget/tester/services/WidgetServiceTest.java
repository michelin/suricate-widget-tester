package com.michelin.suricate.widget.tester.services;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import com.michelin.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import com.michelin.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import com.michelin.suricate.widget.tester.model.dto.api.WidgetParametersRequestDto;
import com.michelin.suricate.widget.tester.model.dto.category.CategoryDto;
import com.michelin.suricate.widget.tester.model.dto.category.CategoryParameterDto;
import com.michelin.suricate.widget.tester.model.dto.nashorn.WidgetVariableResponse;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetDto;
import com.michelin.suricate.widget.tester.services.api.WidgetService;
import com.michelin.suricate.widget.tester.services.nashorn.services.NashornService;
import com.michelin.suricate.widget.tester.utils.WidgetUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WidgetServiceTest {
    @Mock
    private MustacheFactory mustacheFactory;

    @Mock
    private NashornService nashornService;

    @InjectMocks
    private WidgetService widgetService;

    @Test
    void shouldGetWidget() throws IOException {
        try (MockedStatic<WidgetUtils> mocked = mockStatic(WidgetUtils.class)) {
            WidgetDto widgetDto = new WidgetDto();
            widgetDto.setId(1L);
            widgetDto.setName("name");

            CategoryParameterDto categoryParameterDto = new CategoryParameterDto();
            categoryParameterDto.setKey("key");
            categoryParameterDto.setValue("value");

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(1L);
            categoryDto.setConfigurations(Collections.singleton(categoryParameterDto));

            mocked.when(() -> WidgetUtils.getWidget(any()))
                    .thenReturn(widgetDto);
            mocked.when(() -> WidgetUtils.getCategory(any()))
                    .thenReturn(categoryDto);

            WidgetDto actual = widgetService.getWidget("src/test/resources/repository/content/github/widgets/count-issues");

            assertThat(actual.getId()).isEqualTo(1L);
            assertThat(actual.getName()).isEqualTo("name");
            assertThat(actual.getWidgetParams().get(0).getName()).isEqualTo("key");
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
            widgetDto.setHtmlContent("<h1>{{data}}</h1>");

            CategoryParameterDto categoryParameterDto = new CategoryParameterDto();
            categoryParameterDto.setKey("key");
            categoryParameterDto.setValue("value");

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(1L);
            categoryDto.setConfigurations(Collections.singleton(categoryParameterDto));

            WidgetVariableResponse widgetVariableResponse = new WidgetVariableResponse();
            widgetVariableResponse.setName("name");

            mocked.when(() -> WidgetUtils.getWidget(any()))
                    .thenReturn(widgetDto);
            mocked.when(() -> WidgetUtils.getCategory(any()))
                    .thenReturn(categoryDto);
            mocked.when(() -> WidgetUtils.getWidgetParametersForNashorn(any()))
                    .thenReturn(Collections.singletonList(widgetVariableResponse));
            when(nashornService.isNashornRequestExecutable(any()))
                    .thenReturn(true);
            when(mustacheFactory.compile(any(), any()))
                    .thenReturn(new DefaultMustacheFactory().compile(new StringReader(widgetDto.getHtmlContent()), widgetDto.getTechnicalName()));

            WidgetParametersRequestDto widgetParametersRequestDto = new WidgetParametersRequestDto();
            widgetParametersRequestDto.setName("SURI_TITLE");
            widgetParametersRequestDto.setValue("myTitle");

            WidgetExecutionRequestDto widgetExecutionRequestDto = new WidgetExecutionRequestDto();
            widgetExecutionRequestDto.setPath("src/test/resources/repository/content/github/widgets/count-issues");
            widgetExecutionRequestDto.setPreviousData("previousData");
            widgetExecutionRequestDto.setParameters(Collections.singletonList(widgetParametersRequestDto));

            ProjectWidgetResponseDto actual = widgetService.runWidget(widgetExecutionRequestDto);

            assertThat(actual.getInstantiateHtml()).isEqualTo("<h1>test</h1>");
            assertThat(actual.getTechnicalName()).isEqualTo("technicalName");
            assertThat(actual.getCssContent()).isEqualTo("cssContent");
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
            widgetDto.setLibraries(new String[]{"lib"});

            CategoryParameterDto categoryParameterDto = new CategoryParameterDto();
            categoryParameterDto.setKey("key");
            categoryParameterDto.setValue("value");

            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(1L);
            categoryDto.setConfigurations(Collections.singleton(categoryParameterDto));

            mocked.when(() -> WidgetUtils.getWidget(any()))
                    .thenReturn(widgetDto);
            mocked.when(() -> WidgetUtils.getCategory(any()))
                    .thenReturn(categoryDto);
            when(nashornService.isNashornRequestExecutable(any()))
                    .thenReturn(false);
            when(mustacheFactory.compile(any(), any()))
                    .thenReturn(new DefaultMustacheFactory().compile(new StringReader(widgetDto.getHtmlContent()), widgetDto.getTechnicalName()));

            WidgetParametersRequestDto widgetParametersRequestDto = new WidgetParametersRequestDto();
            widgetParametersRequestDto.setName("name");
            widgetParametersRequestDto.setValue("value");

            WidgetExecutionRequestDto widgetExecutionRequestDto = new WidgetExecutionRequestDto();
            widgetExecutionRequestDto.setPath("src/test/resources/repository/content/github/widgets/count-issues");
            widgetExecutionRequestDto.setPreviousData("previousData");
            widgetExecutionRequestDto.setParameters(Collections.singletonList(widgetParametersRequestDto));

            ProjectWidgetResponseDto actual = widgetService.runWidget(widgetExecutionRequestDto);

            assertThat(actual.getInstantiateHtml()).isEqualTo("<h1></h1>");
            assertThat(actual.getTechnicalName()).isEqualTo("technicalName");
            assertThat(actual.getCssContent()).isEqualTo("cssContent");
            assertThat(actual.getLibrariesNames().get(0)).isEqualTo("lib");
        }
    }
}
