package io.suricate.widget.tester.controllers;

import io.suricate.widget.tester.model.dto.library.LibraryDto;
import io.suricate.widget.tester.utils.WidgetUtils;
import io.suricate.widget.tester.utils.exceptions.ObjectNotFoundException;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Asset controller
 */
@RestController
@RequestMapping("/api")
public class LibraryController {

    /**
     * Get the JS library content by name from the widget folder path
     *
     * @param libraryName The library name
     * @return The library data
     */
    @GetMapping(path = "/v1/libraries/{libraryName}/content")
    public ResponseEntity<byte[]> getLibrary(@PathVariable("libraryName") String libraryName,
                                             @RequestParam String widgetPath) {
        File librariesFolder = new File(widgetPath);

        while (!librariesFolder.getPath().endsWith("content")) {
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
