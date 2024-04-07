package com.michelin.suricate.widget.tester.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.michelin.suricate.widget.tester.model.dto.category.CategoryDto;
import com.michelin.suricate.widget.tester.model.dto.js.WidgetVariableResponse;
import com.michelin.suricate.widget.tester.model.dto.library.LibraryDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetParamDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetParamValueDto;
import com.michelin.suricate.widget.tester.model.enumeration.DataTypeEnum;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;

class WidgetUtilsTest {
    @Test
    void shouldParseLibraryFolderNull() {
        List<LibraryDto> actual = WidgetUtils.parseLibraryFolder(null);
        assertNull(actual);
    }

    @Test
    void shouldParseLibraryFolderEmpty() {
        List<LibraryDto> actual = WidgetUtils.parseLibraryFolder(new File("src/test/resources/repository"));
        assertNull(actual);
    }

    @Test
    void shouldParseLibraryFolder() {
        List<LibraryDto> actual = WidgetUtils.parseLibraryFolder(new File("src/test/resources/repository/libraries"));
        assertEquals(1, actual.size());
        assertEquals("test.js", actual.get(0).getTechnicalName());
    }

    @Test
    void shouldGetCategoryNull() throws IOException {
        assertNull(WidgetUtils.getCategory(null));
    }

    @Test
    void shouldGetCategoryWithNoName() throws IOException {
        assertNull(WidgetUtils.getCategory(new File("src/test/resources/specific-repository/content/no-name")));
    }

    @Test
    void shouldGetCategoryWithNoWidgets() throws IOException {
        CategoryDto actual =
            WidgetUtils.getCategory(new File("src/test/resources/specific-repository/content/no-widgets"));

        assertNull(actual.getId());
        assertEquals("noWidgets", actual.getName());
        assertEquals("noWidgets", actual.getTechnicalName());
        assertNotNull(actual.getImage());
    }

    @Test
    void shouldGetCategory() throws IOException {
        CategoryDto actual = WidgetUtils.getCategory(new File("src/test/resources/repository/content/github"));

        assertNull(actual.getId());
        assertEquals("GitHub", actual.getName());
        assertEquals("github", actual.getTechnicalName());
        assertNotNull(actual.getImage());
    }

    @Test
    void shouldGetWidgetNull() throws IOException {
        assertNull(WidgetUtils.getWidget(null));
    }

    @Test
    void shouldGetWidgetNoDelay() throws IOException {
        assertNull(WidgetUtils.getWidget(
                new File("src/test/resources/specific-repository/content/specific-widgets/widgets/no-delay")));
    }

    @Test
    void shouldGetWidgetDelayButNoScript() throws IOException {
        assertNull(WidgetUtils.getWidget(
                new File("src/test/resources/specific-repository/content/"
                    + "specific-widgets/widgets/delay-but-no-script")));
    }

    @Test
    void shouldGetWidgetNoTechnicalName() throws IOException {
        assertNull(WidgetUtils.getWidget(
                new File("src/test/resources/specific-repository/content/"
                    + "specific-widgets/widgets/no-technical-name")));
    }

    @Test
    void shouldGetWidgetGitHubCountIssues() throws IOException {
        WidgetDto actual =
            WidgetUtils.getWidget(new File("src/test/resources/repository/content/github/widgets/count-issues"));

        assertNull(actual.getId());
        assertEquals("Number of issues", actual.getName());
        assertEquals("Display the number of issues of a GitHub project", actual.getDescription());
        assertEquals("githubOpenedIssues", actual.getTechnicalName());
        assertEquals(600L, actual.getDelay());
        assertNotNull(actual.getHtmlContent());
        assertNotNull(actual.getCssContent());
        assertNotNull(actual.getBackendJs());
        assertNotNull(actual.getImage());
        assertEquals(3, actual.getWidgetParams().size());
    }

    @Test
    void shouldGetWidgetClockWithNoParams() throws IOException {
        WidgetDto actual = WidgetUtils.getWidget(new File("src/test/resources/repository/content/other/widgets/clock"));

        assertNull(actual.getId());
        assertEquals("Clock", actual.getName());
        assertEquals("Display the current date and time with a clock", actual.getDescription());
        assertEquals("clock", actual.getTechnicalName());
        assertEquals(-1L, actual.getDelay());
        assertNotNull(actual.getHtmlContent());
        assertNotNull(actual.getCssContent());
        assertNull(actual.getBackendJs());
        assertNotNull(actual.getImage());
        assertTrue(actual.getWidgetParams().isEmpty());
    }

    @Test
    void shouldGetWidgetParametersForJsExecution() {
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

        List<WidgetVariableResponse> actual = WidgetUtils.getWidgetParametersForJsExecution(widget);
        assertEquals(4, actual.size());
    }
}
