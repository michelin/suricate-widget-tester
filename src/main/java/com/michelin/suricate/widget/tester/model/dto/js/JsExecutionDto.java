package com.michelin.suricate.widget.tester.model.dto.js;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import java.util.Date;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Js execution DTO.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class JsExecutionDto extends AbstractDto {
    private String properties;
    private String script;
    private String previousData;
    private Long delay;
    private Long projectId;
    private Long projectWidgetId;
    private boolean alreadySuccess;

    /**
     * Constructor.
     *
     * @param properties   The project widget backend config
     * @param script       The widget js script
     * @param previousData The data of the last execution
     * @param projectId    The project id
     * @param technicalId  The project widget id
     * @param delay        The delay before the next run
     * @param lastSuccess  The last success date
     */
    public JsExecutionDto(String properties, String script, String previousData, Long projectId, Long technicalId,
                          Long delay, Date lastSuccess) {
        this.properties = properties;
        this.script = script;
        this.previousData = previousData;
        this.projectId = projectId;
        this.projectWidgetId = technicalId;
        this.delay = delay;
        this.alreadySuccess = lastSuccess != null;
    }
}
