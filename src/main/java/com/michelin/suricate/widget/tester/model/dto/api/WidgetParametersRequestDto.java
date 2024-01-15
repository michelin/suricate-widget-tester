package com.michelin.suricate.widget.tester.model.dto.api;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Widget parameter request DTO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WidgetParametersRequestDto extends AbstractDto {
    private String name;
    private String value;
}
