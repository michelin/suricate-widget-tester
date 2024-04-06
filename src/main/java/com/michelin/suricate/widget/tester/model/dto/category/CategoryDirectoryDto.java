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
@Builder
@EqualsAndHashCode(callSuper = false)
public class CategoryDirectoryDto extends AbstractDto {
    private String name;
    @Builder.Default
    private Set<String> widgets = new LinkedHashSet<>();
}
