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
        List<File> actual = FilesUtils.getFiles("src/test/resources/repository",
            new File("src/test/resources/repository/libraries"));

        assertEquals(1, actual.size());
        assertEquals("test.js", actual.get(0).getName());
    }

    @Test
    void shouldGetNoFile() throws IOException {
        List<File> actual = FilesUtils.getFiles(null, null);
        assertTrue(actual.isEmpty());
    }

    @Test
    void shouldThrowIoException() {
        assertThrows(IOException.class, () -> FilesUtils.getFiles("src/test/resources/repository",
            new File("src/test/resources/repository/../accessing-resources")));
    }
}
