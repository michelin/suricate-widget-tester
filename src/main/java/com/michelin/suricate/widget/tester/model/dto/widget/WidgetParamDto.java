package com.michelin.suricate.widget.tester.model.dto.widget;

import com.michelin.suricate.widget.tester.model.enumeration.DataTypeEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Widget parameters DTO.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WidgetParamDto {
    private String name;
    private String description;
    private String defaultValue;
    private DataTypeEnum type;
    private String acceptFileRegex;
    private String usageExample;
    private boolean required = true;
    private List<WidgetParamValueDto> possibleValuesMap = new ArrayList<>();
}
