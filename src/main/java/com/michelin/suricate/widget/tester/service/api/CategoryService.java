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
