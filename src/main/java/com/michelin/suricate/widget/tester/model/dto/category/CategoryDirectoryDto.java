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

package com.michelin.suricate.widget.tester.model.dto.category;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Category directory DTO.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class CategoryDirectoryDto extends AbstractDto {
    private String name;
    @Builder.Default
    private Set<String> widgets = new LinkedHashSet<>();
}
