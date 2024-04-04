package com.michelin.suricate.widget.tester.model.dto.category;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Category directory DTO.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryDirectoryDto extends AbstractDto {
    private String name;
    private Set<String> widgets;

    public CategoryDirectoryDto(String name) {
        this.name = name;
        this.widgets = new LinkedHashSet<>();
    }
}
