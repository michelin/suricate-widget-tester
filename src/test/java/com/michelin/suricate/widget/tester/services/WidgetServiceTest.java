package com.michelin.suricate.widget.tester.services;

import com.michelin.suricate.widget.tester.model.dto.category.CategoryDto;
import com.michelin.suricate.widget.tester.model.dto.category.CategoryParameterDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetDto;
import com.michelin.suricate.widget.tester.services.api.WidgetService;
import com.michelin.suricate.widget.tester.utils.WidgetUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WidgetServiceTest {
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
}
