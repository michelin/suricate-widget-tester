package com.michelin.suricate.widget.tester.services.js.tasks;

import static org.assertj.core.api.Assertions.assertThat;

import com.michelin.suricate.widget.tester.model.dto.js.JsExecutionDto;
import com.michelin.suricate.widget.tester.model.dto.js.JsResultDto;
import com.michelin.suricate.widget.tester.model.dto.js.WidgetVariableResponse;
import com.michelin.suricate.widget.tester.model.enums.DataTypeEnum;
import com.michelin.suricate.widget.tester.model.enums.JsExecutionErrorTypeEnum;
import com.michelin.suricate.widget.tester.utils.exceptions.js.FatalException;
import com.michelin.suricate.widget.tester.utils.exceptions.js.RemoteException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class JsExecutionAsyncTaskTest {
    @ParameterizedTest
    @CsvSource({"badScript,ReferenceError: badScript is not defined",
        "function test() {},No run function defined",
        "function run() {},The JSON response is not valid - null",
        "function run () { var file = Java.type('java.io.File'); file.listRoots(); return '{}'},"
            + "TypeError: Access to host class java.io.File is not allowed or does not exist."})
    void shouldFail(String script, String expectedLogs) {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setProjectWidgetId(1L);
        jsExecutionDto.setDelay(0L);
        jsExecutionDto.setPreviousData(null);
        jsExecutionDto.setScript(script);

        JsExecutionAsyncTask task = new JsExecutionAsyncTask(jsExecutionDto, Collections.emptyList());
        JsResultDto actual = task.call();

        assertThat(actual.isFatal()).isTrue();
        assertThat(actual.getLog()).isEqualTo(expectedLogs);
    }

    @ParameterizedTest
    @CsvSource({"function run() {},The JSON response is not valid - null",
        "function run () { Packages.throwError(); return '{}'},Error",
        "function run () { Packages.throwTimeout(); return '{}'},Timeout"})
    void shouldFailWithErrorBecauseBadReturn(String script, String expectedLogs) {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setProjectWidgetId(1L);
        jsExecutionDto.setDelay(0L);
        jsExecutionDto.setPreviousData(null);
        jsExecutionDto.setAlreadySuccess(true);
        jsExecutionDto.setScript(script);

        JsExecutionAsyncTask task = new JsExecutionAsyncTask(jsExecutionDto, Collections.emptyList());
        JsResultDto actual = task.call();

        assertThat(actual.getError()).isEqualTo(JsExecutionErrorTypeEnum.ERROR);
        assertThat(actual.getLog()).isEqualTo(expectedLogs);
    }

    @Test
    void shouldSuccess() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setProjectWidgetId(1L);
        jsExecutionDto.setDelay(0L);
        jsExecutionDto.setPreviousData(null);
        jsExecutionDto.setScript("function run() { return '{}'; }");

        JsExecutionAsyncTask task = new JsExecutionAsyncTask(jsExecutionDto, Collections.emptyList());
        JsResultDto actual = task.call();

        assertThat(actual.getError()).isNull();
        assertThat(actual.isFatal()).isFalse();
        assertThat(actual.getProjectId()).isEqualTo(1L);
        assertThat(actual.getProjectWidgetId()).isEqualTo(1L);
        assertThat(actual.getData()).isEqualTo("{}");
        assertThat(actual.getLog()).isNull();
    }

    @Test
    void shouldSuccessWithWidgetPropertiesWithoutHiding() {
        WidgetVariableResponse widgetParameter = new WidgetVariableResponse();
        widgetParameter.setName("SURI_TITLE");
        widgetParameter.setDescription("title");
        widgetParameter.setType(DataTypeEnum.TEXT);
        widgetParameter.setDefaultValue("defaultValue");
        widgetParameter.setRequired(true);

        WidgetVariableResponse widgetParameterNotRequired = new WidgetVariableResponse();
        widgetParameterNotRequired.setName("NOT_REQUIRED_SURI_TITLE");
        widgetParameterNotRequired.setDescription("not_required_title");
        widgetParameterNotRequired.setType(DataTypeEnum.TEXT);

        List<WidgetVariableResponse> widgetParameters = new ArrayList<>();
        widgetParameters.add(widgetParameter);
        widgetParameters.add(widgetParameterNotRequired);

        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setProjectWidgetId(1L);
        jsExecutionDto.setDelay(0L);
        jsExecutionDto.setPreviousData(null);
        jsExecutionDto.setScript(
            "function run () { print('title='+SURI_TITLE); "
                + "print('notRequiredTitle='+NOT_REQUIRED_SURI_TITLE); return '{}' }");

        JsExecutionAsyncTask task = new JsExecutionAsyncTask(jsExecutionDto, widgetParameters);
        JsResultDto actual = task.call();

        assertThat(actual.getError()).isNull();
        assertThat(actual.isFatal()).isFalse();
        assertThat(actual.getProjectId()).isEqualTo(1L);
        assertThat(actual.getProjectWidgetId()).isEqualTo(1L);
        assertThat(actual.getData()).isEqualTo("{}");
        assertThat(actual.getLog()).contains("title=defaultValue");
        assertThat(actual.getLog()).contains("notRequiredTitle=null");
    }

    @Test
    void shouldSuccessWithLogs() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setProjectWidgetId(1L);
        jsExecutionDto.setDelay(0L);
        jsExecutionDto.setPreviousData(null);
        jsExecutionDto.setScript("function run() { print('This is a log'); return '{}'; }");

        JsExecutionAsyncTask task = new JsExecutionAsyncTask(jsExecutionDto, null);
        JsResultDto actual = task.call();

        assertThat(actual.getError()).isNull();
        assertThat(actual.isFatal()).isFalse();
        assertThat(actual.getProjectId()).isEqualTo(1L);
        assertThat(actual.getProjectWidgetId()).isEqualTo(1L);
        assertThat(actual.getData()).isEqualTo("{}");
        assertThat(actual.getLog()).isEqualTo("This is a log");
    }

    @Test
    void shouldSuccessWithJava() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setProjectWidgetId(1L);
        jsExecutionDto.setDelay(0L);
        jsExecutionDto.setPreviousData(null);
        jsExecutionDto.setScript("function run() { print(Packages.btoa('test')); return '{}'}");

        JsExecutionAsyncTask task = new JsExecutionAsyncTask(jsExecutionDto, Collections.emptyList());
        JsResultDto actual = task.call();

        assertThat(actual.getError()).isNull();
        assertThat(actual.isFatal()).isFalse();
        assertThat(actual.getProjectId()).isEqualTo(1L);
        assertThat(actual.getProjectWidgetId()).isEqualTo(1L);
        assertThat(actual.getData()).isEqualTo("{}");
        assertThat(actual.getLog()).isEqualTo("dGVzdA==");
    }

    @Test
    void shouldBeFatalErrorOrNot() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        JsExecutionAsyncTask task = new JsExecutionAsyncTask(jsExecutionDto, Collections.emptyList());

        assertThat(task.isFatalError(new Exception(""), new Exception(""))).isTrue();
        assertThat(task.isFatalError(new Exception("Error on server"), new Exception("Error on server"))).isTrue();
        assertThat(task.isFatalError(new Exception("timeoutException"), new Exception(""))).isFalse();
        assertThat(task.isFatalError(new Exception("timeoutException"), new FatalException(""))).isFalse();
        assertThat(task.isFatalError(new Exception("timeout:"), new IllegalArgumentException(""))).isFalse();
        assertThat(
            task.isFatalError(new Exception("Error on server"), new RemoteException("Error on server"))).isFalse();
        assertThat(
            task.isFatalError(new Exception("Error on server"), new UnknownHostException("Error on server"))).isFalse();

        jsExecutionDto.setAlreadySuccess(true);

        assertThat(task.isFatalError(new Exception(""), new Exception(""))).isFalse();
        assertThat(task.isFatalError(new Exception("timeoutException"), new Exception(""))).isFalse();
        assertThat(task.isFatalError(new Exception("timeoutException"), new FatalException(""))).isFalse();
        assertThat(
            task.isFatalError(new Exception("Error on server"), new RemoteException("Error on server"))).isFalse();
        assertThat(task.isFatalError(new Exception("Error on server"), new Exception("Error on server"))).isFalse();
        assertThat(
            task.isFatalError(new ConnectException("Connection error"), new IllegalArgumentException())).isFalse();
    }
}
