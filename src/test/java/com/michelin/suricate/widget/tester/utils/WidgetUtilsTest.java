package com.michelin.suricate.widget.tester.utils;

import com.michelin.suricate.widget.tester.model.dto.category.CategoryDto;
import com.michelin.suricate.widget.tester.model.dto.library.LibraryDto;
import com.michelin.suricate.widget.tester.model.dto.nashorn.WidgetVariableResponse;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetParamDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetParamValueDto;
import com.michelin.suricate.widget.tester.model.enums.DataTypeEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class WidgetUtilsTest {
    @Test
    void shouldParseLibraryFolderNull() {
        List<LibraryDto> actual = WidgetUtils.parseLibraryFolder(null);
        assertThat(actual).isNull();
    }

    @Test
    void shouldParseLibraryFolderEmpty() {
        List<LibraryDto> actual = WidgetUtils.parseLibraryFolder(new File("src/test/resources/repository"));
        assertThat(actual).isNull();
    }

    @Test
    void shouldParseLibraryFolder() {
        List<LibraryDto> actual = WidgetUtils.parseLibraryFolder(new File("src/test/resources/repository/libraries"));
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getTechnicalName()).isEqualTo("test.js");
    }

    @Test
    void shouldGetCategoryNull() throws IOException {
        assertThat(WidgetUtils.getCategory(null)).isNull();
    }

    @Test
    void shouldGetCategoryWithNoName() throws IOException {
        assertThat(WidgetUtils.getCategory(new File("src/test/resources/specific-repository/content/no-name"))).isNull();
    }

    @Test
    void shouldGetCategoryWithNoWidgets() throws IOException {
        CategoryDto actual = WidgetUtils.getCategory(new File("src/test/resources/specific-repository/content/no-widgets"));

        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo("noWidgets");
        assertThat(actual.getTechnicalName()).isEqualTo("noWidgets");
        assertThat(actual.getImage()).isNotNull();
    }

    @Test
    void shouldGetCategory() throws IOException {
        CategoryDto actual = WidgetUtils.getCategory(new File("src/test/resources/repository/content/github"));

        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo("GitHub");
        assertThat(actual.getTechnicalName()).isEqualTo("github");
        assertThat(actual.getImage()).isNotNull();
    }

    @Test
    void shouldGetWidgetNull() throws IOException {
        Assertions.assertThat(WidgetUtils.getWidget(null)).isNull();
    }

    @Test
    void shouldGetWidgetNoDelay() throws IOException {
        Assertions.assertThat(WidgetUtils.getWidget(new File("src/test/resources/specific-repository/content/specific-widgets/widgets/no-delay")))
                .isNull();
    }

    @Test
    void shouldGetWidgetDelayButNoScript() throws IOException {
        Assertions.assertThat(WidgetUtils.getWidget(new File("src/test/resources/specific-repository/content/specific-widgets/widgets/delay-but-no-script")))
                .isNull();
    }

    @Test
    void shouldGetWidgetNoTechnicalName() throws IOException {
        Assertions.assertThat(WidgetUtils.getWidget(new File("src/test/resources/specific-repository/content/specific-widgets/widgets/no-technical-name")))
                .isNull();
    }

    @Test
    void shouldGetWidgetGitHubCountIssues() throws IOException {
        WidgetDto actual = WidgetUtils.getWidget(new File("src/test/resources/repository/content/github/widgets/count-issues"));

        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo("Number of issues");
        assertThat(actual.getDescription()).isEqualTo("Display the number of issues of a GitHub project");
        assertThat(actual.getTechnicalName()).isEqualTo("githubOpenedIssues");
        assertThat(actual.getDelay()).isEqualTo(600L);
        assertThat(actual.getHtmlContent()).isNotNull();
        assertThat(actual.getCssContent()).isNotNull();
        assertThat(actual.getBackendJs()).isNotNull();
        assertThat(actual.getImage()).isNotNull();
        assertThat(actual.getWidgetParams()).hasSize(3);
    }

    @Test
    void shouldGetWidgetClockWithNoParams() throws IOException {
        WidgetDto actual = WidgetUtils.getWidget(new File("src/test/resources/repository/content/other/widgets/clock"));

        assertThat(actual.getId()).isNull();
        assertThat(actual.getName()).isEqualTo("Clock");
        assertThat(actual.getDescription()).isEqualTo("Display the current date and time with a clock");
        assertThat(actual.getTechnicalName()).isEqualTo("clock");
        assertThat(actual.getDelay()).isEqualTo(-1L);
        assertThat(actual.getHtmlContent()).isNotNull();
        assertThat(actual.getCssContent()).isNotNull();
        assertThat(actual.getBackendJs()).isNull();
        assertThat(actual.getImage()).isNotNull();
        assertThat(actual.getWidgetParams()).isEmpty();
    }

    @Test
    void shouldGetWidgetParametersForNashorn() {
        WidgetParamDto widgetParam = new WidgetParamDto();
        widgetParam.setName("Name1");
        widgetParam.setDescription("Description");
        widgetParam.setType(DataTypeEnum.TEXT);
        widgetParam.setDefaultValue("defaultValue");

        WidgetParamValueDto widgetParamValue = new WidgetParamValueDto();
        widgetParamValue.setJsKey("key");
        widgetParamValue.setValue("value");

        WidgetParamDto widgetParamTwo = new WidgetParamDto();
        widgetParamTwo.setName("Name2");
        widgetParamTwo.setDescription("Description");
        widgetParamTwo.setType(DataTypeEnum.COMBO);
        widgetParamTwo.setPossibleValuesMap(Collections.singletonList(widgetParamValue));

        WidgetParamDto widgetParamThree = new WidgetParamDto();
        widgetParamThree.setName("Name3");
        widgetParamThree.setDescription("Description");
        widgetParamThree.setType(DataTypeEnum.MULTIPLE);
        widgetParamThree.setPossibleValuesMap(Collections.singletonList(widgetParamValue));

        WidgetParamDto widgetParamFour = new WidgetParamDto();
        widgetParamFour.setName("Name4");
        widgetParamFour.setDescription("Description");
        widgetParamFour.setType(DataTypeEnum.TEXT);
        widgetParamFour.setPossibleValuesMap(Collections.singletonList(widgetParamValue));

        WidgetDto widget = new WidgetDto();
        widget.setId(1L);
        widget.setWidgetParams(Arrays.asList(widgetParam, widgetParamTwo, widgetParamThree, widgetParamFour));

        List<WidgetVariableResponse> actual = WidgetUtils.getWidgetParametersForNashorn(widget);

        assertThat(actual).hasSize(4);
    }
}
