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

package com.michelin.suricate.widget.tester.controller;

import com.michelin.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import com.michelin.suricate.widget.tester.model.dto.api.WidgetExecutionRequestDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetParamDto;
import com.michelin.suricate.widget.tester.model.enumeration.ApiErrorEnum;
import com.michelin.suricate.widget.tester.service.api.WidgetService;
import com.michelin.suricate.widget.tester.util.exception.ApiException;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Widget controller.
 */
@RestController
@RequestMapping("/api")
public class WidgetController {
    @Autowired
    private WidgetService widgetService;

    /**
     * Get the widget parameters according to a given path.
     *
     * @param category The category
     * @param widget   The widget
     * @return The widget parameters
     * @throws IOException Exception thrown if an error occurred while reading the widget file
     */
    @GetMapping(value = "/v1/widgets/parameters")
    public ResponseEntity<List<WidgetParamDto>> getWidgetParameters(@RequestParam String category,
                                                                    @RequestParam String widget) throws IOException {
        WidgetDto widgetDto = widgetService.getWidget(category, widget);

        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(widgetDto.getWidgetParams());
    }

    /**
     * Run the widget configured in the application properties.
     *
     * @param widgetExecutionRequestDto The widget execution request
     * @return The widget execution response
     * @throws IOException Exception thrown if an error occurred while reading the widget file
     */
    @PostMapping(value = "/v1/widgets/run")
    public ResponseEntity<ProjectWidgetResponseDto> runWidget(
        @RequestBody WidgetExecutionRequestDto widgetExecutionRequestDto) throws IOException {
        ProjectWidgetResponseDto projectWidgetResponseDto = widgetService.runWidget(widgetExecutionRequestDto);

        if (projectWidgetResponseDto.getLog() != null) {
            throw new ApiException(projectWidgetResponseDto.getLog(), ApiErrorEnum.INTERNAL_SERVER_ERROR);
        }

        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(projectWidgetResponseDto);
    }
}
