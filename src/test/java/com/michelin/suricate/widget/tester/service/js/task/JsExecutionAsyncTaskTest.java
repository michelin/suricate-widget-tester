/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.michelin.suricate.widget.tester.service.js.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.michelin.suricate.widget.tester.model.dto.js.JsExecutionDto;
import com.michelin.suricate.widget.tester.model.dto.js.JsResultDto;
import com.michelin.suricate.widget.tester.model.dto.js.WidgetVariableResponse;
import com.michelin.suricate.widget.tester.model.enumeration.DataTypeEnum;
import com.michelin.suricate.widget.tester.model.enumeration.JsExecutionErrorTypeEnum;
import com.michelin.suricate.widget.tester.util.exception.js.FatalException;
import com.michelin.suricate.widget.tester.util.exception.js.RemoteException;
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
    @CsvSource({
        "badScript,ReferenceError: badScript is not defined",
        "function test() {},No run function defined",
        "function run() {},The JSON response is not valid - null",
        "function run () { var file = Java.type('java.io.File'); file.listRoots(); return '{}'},"
                + "TypeError: Access to host class java.io.File is not allowed or does not exist."
    })
    void shouldFail(String script, String expectedLogs) {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setProjectWidgetId(1L);
        jsExecutionDto.setDelay(0L);
        jsExecutionDto.setPreviousData(null);
        jsExecutionDto.setScript(script);

        JsExecutionAsyncTask task = new JsExecutionAsyncTask(jsExecutionDto, Collections.emptyList());
        JsResultDto actual = task.call();

        assertTrue(actual.isFatal());
        assertEquals(expectedLogs, actual.getLog());
    }

    @ParameterizedTest
    @CsvSource({
        "function run() {},The JSON response is not valid - null",
        "function run () { Packages.throwError(); return '{}'},Error",
        "function run () { Packages.throwTimeout(); return '{}'},Timeout"
    })
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

        assertEquals(JsExecutionErrorTypeEnum.ERROR, actual.getError());
        assertEquals(expectedLogs, actual.getLog());
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

        assertNull(actual.getError());
        assertFalse(actual.isFatal());
        assertEquals(1L, actual.getProjectId());
        assertEquals(1L, actual.getProjectWidgetId());
        assertEquals("{}", actual.getData());
        assertNull(actual.getLog());
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
        jsExecutionDto.setScript("function run () { print('title='+SURI_TITLE); "
                + "print('notRequiredTitle='+NOT_REQUIRED_SURI_TITLE); return '{}' }");

        JsExecutionAsyncTask task = new JsExecutionAsyncTask(jsExecutionDto, widgetParameters);
        JsResultDto actual = task.call();

        assertNull(actual.getError());
        assertFalse(actual.isFatal());
        assertEquals(1L, actual.getProjectId());
        assertEquals(1L, actual.getProjectWidgetId());
        assertEquals("{}", actual.getData());
        assertTrue(actual.getLog().contains("title=defaultValue"));
        assertTrue(actual.getLog().contains("notRequiredTitle=null"));
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

        assertNull(actual.getError());
        assertFalse(actual.isFatal());
        assertEquals(1L, actual.getProjectId());
        assertEquals(1L, actual.getProjectWidgetId());
        assertEquals("{}", actual.getData());
        assertEquals("This is a log", actual.getLog());
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

        assertNull(actual.getError());
        assertFalse(actual.isFatal());
        assertEquals(1L, actual.getProjectId());
        assertEquals(1L, actual.getProjectWidgetId());
        assertEquals("{}", actual.getData());
        assertEquals("dGVzdA==", actual.getLog());
    }

    @Test
    void shouldBeFatalErrorOrNot() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        JsExecutionAsyncTask task = new JsExecutionAsyncTask(jsExecutionDto, Collections.emptyList());

        assertTrue(task.isFatalError(new Exception(""), new Exception("")));
        assertTrue(task.isFatalError(new Exception("Error on server"), new Exception("Error on server")));
        assertFalse(task.isFatalError(new Exception("timeoutException"), new Exception("")));
        assertFalse(task.isFatalError(new Exception("timeoutException"), new FatalException("")));
        assertFalse(task.isFatalError(new Exception("timeout:"), new IllegalArgumentException("")));
        assertFalse(task.isFatalError(new Exception("Error on server"), new RemoteException("Error on server")));
        assertFalse(task.isFatalError(new Exception("Error on server"), new UnknownHostException("Error on server")));

        jsExecutionDto.setAlreadySuccess(true);

        assertFalse(task.isFatalError(new Exception(""), new Exception("")));
        assertFalse(task.isFatalError(new Exception("timeoutException"), new Exception("")));
        assertFalse(task.isFatalError(new Exception("timeoutException"), new FatalException("")));
        assertFalse(task.isFatalError(new Exception("Error on server"), new RemoteException("Error on server")));
        assertFalse(task.isFatalError(new Exception("Error on server"), new Exception("Error on server")));
        assertFalse(task.isFatalError(new ConnectException("Connection error"), new IllegalArgumentException()));
    }
}
