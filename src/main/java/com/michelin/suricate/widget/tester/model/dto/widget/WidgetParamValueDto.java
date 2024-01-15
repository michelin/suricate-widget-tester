package com.michelin.suricate.widget.tester.model.dto.widget;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Widget param value DTO.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WidgetParamValueDto {
    private String jsKey;
    private String value;
}
