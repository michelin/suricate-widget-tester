package com.michelin.suricate.widget.tester.model.dto.category;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Category DTO
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CategoryDto extends AbstractDto {

    /**
     * The category id
     */
    private Long id;

    /**
     * The category name
     */
    private String name;

    /**
     * The technical name
     */
    private String technicalName;

    /**
     * The image related to this category
     */
    private byte[] image;

    /**
     * The associated categories for this configuration
     */
    private Set<CategoryParameterDto> configurations = new LinkedHashSet<>();
}
