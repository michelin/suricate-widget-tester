package com.michelin.suricate.widget.tester.controller;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.when;

import com.michelin.suricate.widget.tester.model.dto.category.CategoryDirectoryDto;
import com.michelin.suricate.widget.tester.service.api.CategoryService;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {
    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void shouldGetCategoryDirectories() throws IOException {
        CategoryDirectoryDto categoryDirectoryDto = CategoryDirectoryDto.builder()
            .name("categoryName")
            .build();

        when(categoryService.getCategoryDirectories())
            .thenReturn(List.of(categoryDirectoryDto));

        ResponseEntity<List<CategoryDirectoryDto>> actual = categoryController.getCategoryDirectories();

        assertIterableEquals(List.of(categoryDirectoryDto), actual.getBody());
    }
}
