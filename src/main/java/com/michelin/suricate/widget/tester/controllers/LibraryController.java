package com.michelin.suricate.widget.tester.controllers;

import com.michelin.suricate.widget.tester.model.dto.library.LibraryDto;
import com.michelin.suricate.widget.tester.utils.WidgetUtils;
import com.michelin.suricate.widget.tester.utils.exceptions.ObjectNotFoundException;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Library controller.
 */
@RestController
@RequestMapping("/api")
public class LibraryController {

    /**
     * Get the JS library content by name from the widget folder path.
     *
     * @param libraryName The library name
     * @return The library data
     */
    @GetMapping(path = "/v1/libraries/{libraryName}/content")
    public ResponseEntity<byte[]> getLibrary(@PathVariable("libraryName") String libraryName,
                                             @RequestParam String widgetPath) throws IOException {
        File librariesFolder = new File(widgetPath);

        while (!librariesFolder.getCanonicalPath().endsWith("content")) {
            librariesFolder = librariesFolder.getParentFile();
        }

        librariesFolder = new File(librariesFolder.getParentFile().getPath() + "/libraries");

        LibraryDto libraryDto = WidgetUtils.parseLibraryFolder(librariesFolder)
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
