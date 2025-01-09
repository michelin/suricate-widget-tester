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

package com.michelin.suricate.widget.tester.service.api;

import com.michelin.suricate.widget.tester.model.dto.category.CategoryDirectoryDto;
import com.michelin.suricate.widget.tester.property.ApplicationProperties;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Category service.
 */
@Service
public class CategoryService {
    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * Get the category directories.
     *
     * @return The category directories
     * @throws IOException Exception thrown if an error occurred while reading the category directories
     */
    public List<CategoryDirectoryDto> getCategoryDirectories() throws IOException {
        List<CategoryDirectoryDto> categoryDirectoryDtos = new ArrayList<>();

        File widgetsDir = new File(applicationProperties.getWidgets().getRepository() + "/content");
        try (Stream<Path> categories = Files.list(Paths.get(widgetsDir.getCanonicalPath()))) {
            List<File> categoriesFile = categories.map(Path::toFile)
                .filter(File::isDirectory)
                .sorted()
                .toList();

            for (File category : categoriesFile) {
                CategoryDirectoryDto categoryDirectoryDto = CategoryDirectoryDto.builder()
                    .name(category.getName())
                    .build();

                try (Stream<Path> list = Files.list(Paths.get(category.getCanonicalPath() + "/widgets"))) {
                    categoryDirectoryDto.getWidgets().addAll(list.map(Path::toFile)
                        .filter(File::isDirectory)
                        .sorted()
                        .map(File::getName)
                        .toList());
                }
                categoryDirectoryDtos.add(categoryDirectoryDto);
            }
        }

        return categoryDirectoryDtos;
    }
}
