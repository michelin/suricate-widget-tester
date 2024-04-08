package com.michelin.suricate.widget.tester.controller;

import com.michelin.suricate.widget.tester.model.dto.category.CategoryDirectoryDto;
import com.michelin.suricate.widget.tester.service.api.CategoryService;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Category controller.
 */
@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * Get the category directories.
     *
     * @return The category directories
     * @throws IOException Exception thrown if an error occurred while reading the category directories
     */
    @GetMapping(value = "/v1/categories/directories")
    public ResponseEntity<List<CategoryDirectoryDto>> getCategoryDirectories() throws IOException {
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(categoryService.getCategoryDirectories());
    }
}
