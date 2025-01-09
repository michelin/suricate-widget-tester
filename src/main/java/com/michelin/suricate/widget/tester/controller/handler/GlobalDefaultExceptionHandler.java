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

package com.michelin.suricate.widget.tester.controller.handler;

import com.michelin.suricate.widget.tester.model.dto.error.ApiErrorDto;
import com.michelin.suricate.widget.tester.model.enumeration.ApiErrorEnum;
import com.michelin.suricate.widget.tester.util.exception.ApiException;
import java.nio.file.NoSuchFileException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Rest controller advice used to manage exceptions.
 */
@Slf4j
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * Manage the API exception.
     *
     * @param exception The exception
     * @return The exception as Response Entity
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorDto> handleApiException(ApiException exception) {
        log.debug("An exception has occurred in the API controllers part", exception);

        return ResponseEntity
            .status(exception.getError().getStatus())
            .body(exception.getError());
    }

    /**
     * Manage the API exception.
     *
     * @param exception The exception
     * @return The exception as Response Entity
     */
    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<ApiErrorDto> handleApiException(NoSuchFileException exception) {
        log.debug("An exception has occurred in the API controllers part", exception);

        return ResponseEntity
            .status(ApiErrorEnum.FILE_ERROR.getStatus())
            .body(new ApiErrorDto(String.format("The file %s does not exist", exception.getMessage()),
                ApiErrorEnum.FILE_ERROR));
    }
}
