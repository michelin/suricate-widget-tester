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
     * @param folder The folder containing the files
     * @return The list of files
     * @throws IOException Exception triggered during the files fetching
     */
    public static List<File> getFiles(String rootPath, File folder) throws IOException {
        if (folder != null) {
            if (!folder.toPath().normalize().startsWith(rootPath)) {
                throw new IOException("Entry is outside of the target director");
            }

            try (Stream<Path> list = Files.list(Paths.get(folder.getCanonicalPath()))) {
                return list.map(Path::toFile)
                        .filter(File::isFile)
                        .sorted()
                        .toList();
            }
        }

        return Collections.emptyList();
    }
}
