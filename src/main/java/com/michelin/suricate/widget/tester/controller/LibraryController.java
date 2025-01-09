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

import com.michelin.suricate.widget.tester.model.dto.library.LibraryDto;
import com.michelin.suricate.widget.tester.property.ApplicationProperties;
import com.michelin.suricate.widget.tester.util.WidgetUtils;
import com.michelin.suricate.widget.tester.util.exception.ObjectNotFoundException;
import java.io.File;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Library controller.
 */
@RestController
@RequestMapping("/api")
public class LibraryController {
    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * Get the JS library content by name from the widget folder path.
     *
     * @param libraryName The library name
     * @return The library data
     */
    @GetMapping(path = "/v1/libraries/{libraryName}/content")
    public ResponseEntity<byte[]> getLibrary(@PathVariable("libraryName") String libraryName) {
        String rootFolder = applicationProperties.getWidgets().getRepository();
        File librariesFolder = new File(rootFolder + "/libraries");

        LibraryDto libraryDto = WidgetUtils.parseLibraryFolder(rootFolder, librariesFolder)
            .stream()
            .filter(library -> library.getTechnicalName().equals(libraryName))
            .findFirst()
            .orElse(null);

        if (libraryDto == null) {
            throw new ObjectNotFoundException(LibraryDto.class, libraryName);
        }

        return ResponseEntity
            .ok()
            .contentType(MediaType.parseMediaType("application/javascript"))
            .contentLength(libraryDto.getAsset().length)
            .lastModified(new Date().getTime())
            .cacheControl(CacheControl.noCache())
            .body(libraryDto.getAsset());
    }
}
