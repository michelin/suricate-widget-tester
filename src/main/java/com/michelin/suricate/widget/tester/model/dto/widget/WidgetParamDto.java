package com.michelin.suricate.widget.tester.model.dto.widget;

import com.michelin.suricate.widget.tester.model.enums.DataTypeEnum;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Widget parameters DTO
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WidgetParamDto {

    /**
     * The param name
     */
    private String name;

    /**
     * The param description
     */
    private String description;

    /**
     * The default value of the param
     */
    private String defaultValue;

    /**
     * The param type {@link DataTypeEnum}
     */
    private DataTypeEnum type;

    /**
     * The regex used for accept a file while uploading it if the type is a FILE
     */
    private String acceptFileRegex;

    /**
     * An example of the usage of this param
     */
    private String usageExample;

    /**
     * If the param is required True by default
     */
    private boolean required = true;

    /**
     * The list of param values if the type is COMBO or a MULTIPLE
     */
    private List<WidgetParamValueDto> possibleValuesMap = new ArrayList<>();
}
