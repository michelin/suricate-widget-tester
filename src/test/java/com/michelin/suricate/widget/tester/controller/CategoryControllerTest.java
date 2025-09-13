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

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

import com.michelin.suricate.widget.tester.model.dto.category.CategoryDirectoryDto;
import com.michelin.suricate.widget.tester.service.api.HttpCategoryService;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @Mock
    private HttpCategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void shouldGetCategoryDirectories() throws IOException {
        CategoryDirectoryDto categoryDirectoryDto =
                CategoryDirectoryDto.builder().name("categoryName").build();

        when(categoryService.getCategoryDirectories()).thenReturn(List.of(categoryDirectoryDto));

        ResponseEntity<List<CategoryDirectoryDto>> actual = categoryController.getCategoryDirectories();

        assertIterableEquals(List.of(categoryDirectoryDto), actual.getBody());
    }
}
