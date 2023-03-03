package com.michelin.suricate.widget.tester.model.dto.nashorn;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import com.michelin.suricate.widget.tester.model.enums.DataTypeEnum;
import lombok.*;

import java.util.Map;

/**
 * Widget variable used for communication with the clients via webservices
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class WidgetVariableResponse extends AbstractDto {

    /**
     * The variable name
     */
    private String name;

    /**
     * The variable description
     */
    private String description;

    /**
     * The data
     */
    private String data;

    /**
     * The variable type
     */
    private DataTypeEnum type;

    /**
     * If the variable is required
     */
    private boolean required;

    /**
     * The default value
     */
    private String defaultValue;

    /**
     * Map of values
     */
    private Map<String, String> values;
}
