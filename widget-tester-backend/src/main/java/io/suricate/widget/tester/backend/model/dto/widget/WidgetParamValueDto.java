package io.suricate.widget.tester.backend.model.dto.widget;

import lombok.*;

/**
 * Widget param value DTO
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class WidgetParamValueDto {
    /**
     * The key used in the js file
     */
    private String jsKey;

    /**
     * The value of this param
     */
    private String value;
}
