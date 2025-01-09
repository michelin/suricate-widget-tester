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

package com.michelin.suricate.widget.tester.model.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import com.michelin.suricate.widget.tester.model.enumeration.ApiErrorEnum;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Api error DTO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApiErrorDto extends AbstractDto {
    private String message;
    private String key;
    private int status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    /**
     * Constructor.
     *
     * @param apiErrorEnum The API error enum
     */
    public ApiErrorDto(ApiErrorEnum apiErrorEnum) {
        this.message = apiErrorEnum.getMessage();
        this.key = apiErrorEnum.getKey();
        this.timestamp = new Date();
        this.status = apiErrorEnum.getStatus().value();
    }

    /**
     * Constructor.
     *
     * @param message  The error message
     * @param apiError The API error enum
     */
    public ApiErrorDto(String message, ApiErrorEnum apiError) {
        this(apiError);
        this.message = StringUtils.isBlank(message) ? apiError.getMessage() : message;
    }
}
