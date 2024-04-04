package com.michelin.suricate.widget.tester.model.dto.js;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import com.michelin.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import com.michelin.suricate.widget.tester.model.enumeration.JsExecutionErrorTypeEnum;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Js result DTO.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class JsResultDto extends AbstractDto {
    private String data;
    private String log;
    private Long projectId;
    private Long projectWidgetId;
    private Date launchDate;
    private JsExecutionErrorTypeEnum error;
    private ProjectWidgetResponseDto projectWidget;

    /**
     * Check if the Js error result type is fatal.
     *
     * @return true if the Js error result is fatal, false otherwise
     */
    public boolean isFatal() {
        return JsExecutionErrorTypeEnum.FATAL == error;
    }
}
