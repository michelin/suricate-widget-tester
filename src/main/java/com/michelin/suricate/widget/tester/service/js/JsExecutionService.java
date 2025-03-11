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
package com.michelin.suricate.widget.tester.service.js;

import com.michelin.suricate.widget.tester.model.dto.js.JsExecutionDto;
import com.michelin.suricate.widget.tester.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/** Js execution service. */
@Slf4j
@Service
public class JsExecutionService {
    /**
     * Check if the given Js execution can be executed.
     *
     * @param jsExecutionDto The Js execution to check
     * @return True is it is ok, false otherwise
     */
    public boolean isJsExecutable(final JsExecutionDto jsExecutionDto) {
        if (!StringUtils.isNotEmpty(jsExecutionDto.getScript())) {
            log.debug(
                    "The widget instance {} has no script. Stopping JavaScript execution",
                    jsExecutionDto.getProjectWidgetId());
            return false;
        }

        if (!JsonUtils.isValid(jsExecutionDto.getPreviousData())) {
            log.debug(
                    "The widget instance {} has bad formed previous data. Stopping JavaScript execution",
                    jsExecutionDto.getProjectWidgetId());
            return false;
        }

        if (jsExecutionDto.getDelay() == null || jsExecutionDto.getDelay() < 0) {
            log.debug(
                    "The widget instance {} has no delay or delay is < 0. Stopping JavaScript execution",
                    jsExecutionDto.getProjectWidgetId());
            return false;
        }

        return true;
    }
}
