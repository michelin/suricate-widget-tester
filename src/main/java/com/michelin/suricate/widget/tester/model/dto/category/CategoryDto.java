package com.michelin.suricate.widget.tester.model.dto.category;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetDto;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Category DTO.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CategoryDto extends AbstractDto {
    private Long id;
    private String name;
    private String technicalName;
    private byte[] image;
    private Set<CategoryParameterDto> configurations = new LinkedHashSet<>();
    private Set<WidgetDto> widgets = new LinkedHashSet<>();
}
