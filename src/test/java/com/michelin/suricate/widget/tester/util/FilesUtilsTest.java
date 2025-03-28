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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class FilesUtilsTest {
    @Test
    void shouldGetFiles() throws IOException {
        List<File> actual = FilesUtils.getFiles(
                "src/test/resources/repository/", new File("src/test/resources/repository/libraries"));

        assertEquals(1, actual.size());
        assertEquals("test.js", actual.get(0).getName());
    }

    @Test
    void shouldGetNoFile() throws IOException {
        List<File> actual = FilesUtils.getFiles(null, null);
        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldThrowIoExceptionWhenOutside() {
        assertThrows(
                IOException.class,
                () -> FilesUtils.getFiles(
                        "src/test/resources/repository",
                        new File("src/test/resources/repository/../accessing-resources")));
    }

    @Test
    void shouldThrowIoExceptionOnPartialPathTraversal() {
        assertThrows(
                IOException.class,
                () -> FilesUtils.getFiles("src/test/resources/repo", new File("src/test/resources/repository/")));
    }
}
