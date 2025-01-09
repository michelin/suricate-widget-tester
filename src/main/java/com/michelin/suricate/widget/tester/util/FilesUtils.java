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

package com.michelin.suricate.widget.tester.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Files utils.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FilesUtils {
    /**
     * Get all the files inside a given folder.
     *
     * @param rootPath The root path
     * @param folder   The folder containing the files
     * @return The list of files
     * @throws IOException Exception triggered during the files fetching
     */
    public static List<File> getFiles(String rootPath, File folder) throws IOException {
        if (folder != null) {
            if (!rootPath.endsWith(File.separator)) {
                rootPath = rootPath + File.separator;
            }

            if (!folder.toPath().normalize().startsWith(rootPath)) {
                throw new IOException("Entry is outside of the target director");
            }

            String folderPath = folder.getCanonicalPath();
            try (Stream<Path> list = Files.list(Paths.get(folderPath))) {
                return list.map(Path::toFile)
                    .filter(File::isFile)
                    .sorted()
                    .toList();
            }
        }

        return Collections.emptyList();
    }
}
