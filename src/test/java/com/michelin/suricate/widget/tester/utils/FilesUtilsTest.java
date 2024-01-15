package com.michelin.suricate.widget.tester.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;

class FilesUtilsTest {
    @Test
    void shouldGetFiles() throws IOException {
        List<File> actual = FilesUtils.getFiles(new File("src/test/resources/repository/libraries"));

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getName()).contains("test.js");
    }

    @Test
    void shouldGetNoFile() throws IOException {
        List<File> actual = FilesUtils.getFiles(null);

        assertThat(actual).isEmpty();
    }
}
