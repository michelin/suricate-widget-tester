package com.michelin.suricate.widget.tester.services.nashorn.tasks;

import com.michelin.suricate.widget.tester.model.dto.nashorn.NashornRequest;
import com.michelin.suricate.widget.tester.model.dto.nashorn.NashornResponse;
import com.michelin.suricate.widget.tester.model.dto.nashorn.WidgetVariableResponse;
import com.michelin.suricate.widget.tester.model.enums.DataTypeEnum;
import com.michelin.suricate.widget.tester.model.enums.NashornErrorTypeEnum;
import com.michelin.suricate.widget.tester.utils.exceptions.nashorn.FatalException;
import com.michelin.suricate.widget.tester.utils.exceptions.nashorn.RemoteException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NashornRequestWidgetExecutionAsyncTaskTest {
    @ParameterizedTest
    @CsvSource({"badScript,ReferenceError: \"badScript\" is not defined",
            "function test() {},No such function run",
            "function run() {},The JSON response is not valid - null",
            "function run () { var file = Java.type('java.io.File'); file.listRoots(); return '{}'},java.io.File"})
    void shouldFail(String script, String expectedLogs) {
        NashornRequest nashornRequest = new NashornRequest();
        nashornRequest.setProjectId(1L);
        nashornRequest.setProjectWidgetId(1L);
        nashornRequest.setDelay(0L);
        nashornRequest.setPreviousData(null);
        nashornRequest.setScript(script);

        NashornRequestWidgetExecutionAsyncTask task = new NashornRequestWidgetExecutionAsyncTask(nashornRequest, Collections.emptyList());
        NashornResponse actual = task.call();

        assertThat(actual.isFatal()).isTrue();
        assertThat(actual.getLog()).isEqualTo(expectedLogs);
    }

    @ParameterizedTest
    @CsvSource({"function run() {},The JSON response is not valid - null",
            "function run () { Packages.throwError(); return '{}'},Error",
            "function run () { Packages.throwTimeout(); return '{}'},Timeout"})
    void shouldFailWithErrorBecauseBadReturn(String script, String expectedLogs) {
        NashornRequest nashornRequest = new NashornRequest();
        nashornRequest.setProjectId(1L);
        nashornRequest.setProjectWidgetId(1L);
        nashornRequest.setDelay(0L);
        nashornRequest.setPreviousData(null);
        nashornRequest.setAlreadySuccess(true);
        nashornRequest.setScript(script);

        NashornRequestWidgetExecutionAsyncTask task = new NashornRequestWidgetExecutionAsyncTask(nashornRequest, Collections.emptyList());
        NashornResponse actual = task.call();

        assertThat(actual.getError()).isEqualTo(NashornErrorTypeEnum.ERROR);
        assertThat(actual.getLog()).isEqualTo(expectedLogs);
    }

    @Test
    void shouldSuccess() {
        NashornRequest nashornRequest = new NashornRequest();
        nashornRequest.setProjectId(1L);
        nashornRequest.setProjectWidgetId(1L);
        nashornRequest.setDelay(0L);
        nashornRequest.setPreviousData(null);
        nashornRequest.setScript("function run() { return '{}'; }");

        NashornRequestWidgetExecutionAsyncTask task = new NashornRequestWidgetExecutionAsyncTask(nashornRequest, Collections.emptyList());
        NashornResponse actual = task.call();

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

        NashornRequest nashornRequest = new NashornRequest();
        nashornRequest.setProjectId(1L);
        nashornRequest.setProjectWidgetId(1L);
        nashornRequest.setDelay(0L);
        nashornRequest.setPreviousData(null);
        nashornRequest.setScript("function run () { print('title='+SURI_TITLE); print('notRequiredTitle='+NOT_REQUIRED_SURI_TITLE); return '{}' }");

        NashornRequestWidgetExecutionAsyncTask task = new NashornRequestWidgetExecutionAsyncTask(nashornRequest, widgetParameters);
        NashornResponse actual = task.call();

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
        NashornRequest nashornRequest = new NashornRequest();
        nashornRequest.setProjectId(1L);
        nashornRequest.setProjectWidgetId(1L);
        nashornRequest.setDelay(0L);
        nashornRequest.setPreviousData(null);
        nashornRequest.setScript("function run() { print('This is a log'); return '{}'; }");

        NashornRequestWidgetExecutionAsyncTask task = new NashornRequestWidgetExecutionAsyncTask(nashornRequest, null);
        NashornResponse actual = task.call();

        assertThat(actual.getError()).isNull();
        assertThat(actual.isFatal()).isFalse();
        assertThat(actual.getProjectId()).isEqualTo(1L);
        assertThat(actual.getProjectWidgetId()).isEqualTo(1L);
        assertThat(actual.getData()).isEqualTo("{}");
        assertThat(actual.getLog()).isEqualTo("This is a log");
    }

    @Test
    void shouldSuccessWithJava() {
        NashornRequest nashornRequest = new NashornRequest();
        nashornRequest.setProjectId(1L);
        nashornRequest.setProjectWidgetId(1L);
        nashornRequest.setDelay(0L);
        nashornRequest.setPreviousData(null);
        nashornRequest.setScript("function run() { print(Packages.btoa('test')); return '{}'}");

        NashornRequestWidgetExecutionAsyncTask task = new NashornRequestWidgetExecutionAsyncTask(nashornRequest, Collections.emptyList());
        NashornResponse actual = task.call();

        assertThat(actual.getError()).isNull();
        assertThat(actual.isFatal()).isFalse();
        assertThat(actual.getProjectId()).isEqualTo(1L);
        assertThat(actual.getProjectWidgetId()).isEqualTo(1L);
        assertThat(actual.getData()).isEqualTo("{}");
        assertThat(actual.getLog()).isEqualTo("dGVzdA==");
    }

    @Test
    void shouldBeFatalErrorOrNot() {
        NashornRequest nashornRequest = new NashornRequest();
        NashornRequestWidgetExecutionAsyncTask task = new NashornRequestWidgetExecutionAsyncTask(nashornRequest, Collections.emptyList());

        assertThat(task.isFatalError(new Exception(""), new Exception(""))).isTrue();
        assertThat(task.isFatalError(new Exception("Error on server"), new Exception("Error on server"))).isTrue();
        assertThat(task.isFatalError(new Exception("timeoutException"), new Exception(""))).isFalse();
        assertThat(task.isFatalError(new Exception("timeoutException"), new FatalException(""))).isFalse();
        assertThat(task.isFatalError(new Exception("timeout:"), new IllegalArgumentException(""))).isFalse();
        assertThat(task.isFatalError(new Exception("Error on server"), new RemoteException("Error on server"))).isFalse();
        assertThat(task.isFatalError(new Exception("Error on server"), new UnknownHostException("Error on server"))).isFalse();

        nashornRequest.setAlreadySuccess(true);

        assertThat(task.isFatalError(new Exception(""), new Exception(""))).isFalse();
        assertThat(task.isFatalError(new Exception("timeoutException"), new Exception(""))).isFalse();
        assertThat(task.isFatalError(new Exception("timeoutException"), new FatalException(""))).isFalse();
        assertThat(task.isFatalError(new Exception("Error on server"), new RemoteException("Error on server"))).isFalse();
        assertThat(task.isFatalError(new Exception("Error on server"), new Exception("Error on server"))).isFalse();
        assertThat(task.isFatalError(new ConnectException("Connection error"), new IllegalArgumentException())).isFalse();
    }
}
