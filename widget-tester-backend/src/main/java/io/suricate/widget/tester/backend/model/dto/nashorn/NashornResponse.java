package io.suricate.widget.tester.backend.model.dto.nashorn;

import io.suricate.widget.tester.backend.model.dto.AbstractDto;
import io.suricate.widget.tester.backend.model.enums.NashornErrorTypeEnum;
import io.suricate.widget.tester.backend.utils.JsonUtils;
import lombok.*;

import java.util.Date;

/**
 * Response after Nashorn execution
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class NashornResponse extends AbstractDto {

    /**
     * The new calculated data
     */
    private String data;

    /**
     * the log data
     */
    private String log;

    /**
     * The project ID
     */
    private Long projectId;

    /**
     * The projectWidget ID
     */
    private Long projectWidgetId;

    /**
     * Launch date
     */
    private Date launchDate;

    /**
     * Error
     */
    private NashornErrorTypeEnum error;
}
