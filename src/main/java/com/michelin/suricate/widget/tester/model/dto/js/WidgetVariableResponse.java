package com.michelin.suricate.widget.tester.model.dto.js;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import com.michelin.suricate.widget.tester.model.enumeration.DataTypeEnum;
import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Widget variable response DTO.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class WidgetVariableResponse extends AbstractDto {
    private String name;
    private String description;
    private String data;
    private DataTypeEnum type;
    private boolean required;
    private String defaultValue;
    private Map<String, String> values;
}
