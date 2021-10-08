package io.suricate.widget.tester.model.dto.nashorn;

import io.suricate.widget.tester.model.dto.AbstractDto;
import io.suricate.widget.tester.utils.JsonUtils;
import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Nashorn request, used to execute the widget JS, with Nashorn
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class NashornRequest extends AbstractDto {

    /**
     * Nashorn Properties
     */
    private String properties;

    /**
     * javascript script
     */
    private String script;

    /**
     * Previous json data
     */
    private String previousData;

    /**
     * The delay to launch the script
     */
    private Long delay;

    /**
     * project id
     */
    private Long projectId;

    /**
     * Project widget ID
     */
    private Long projectWidgetId;

    /**
     * Boolean to indicate if the
     */
    private boolean alreadySuccess;

    /**
     * Constructor
     *
     * @param properties The project widget backend config
     * @param script The widget js script
     * @param previousData The data of the last execution
     * @param projectId The project id
     * @param technicalId The project widget id
     * @param delay The delay before the next run
     * @param lastSuccess The last success date
     */
    public NashornRequest(String properties, String script, String previousData, Long projectId, Long technicalId, Long delay, Date lastSuccess) {
        this.properties = properties;
        this.script = script;
        this.previousData = previousData;
        this.projectId = projectId;
        this.projectWidgetId = technicalId;
        this.delay = delay;
        this.alreadySuccess = lastSuccess != null;
    }
}
