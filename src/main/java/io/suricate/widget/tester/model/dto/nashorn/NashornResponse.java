package io.suricate.widget.tester.model.dto.nashorn;

import io.suricate.widget.tester.model.dto.AbstractDto;
import io.suricate.widget.tester.model.dto.api.ProjectWidgetResponseDto;
import io.suricate.widget.tester.model.dto.widget.WidgetDto;
import io.suricate.widget.tester.model.enums.NashornErrorTypeEnum;
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

    /**
    * The "fake" project widget to return through the web service
    */
    private ProjectWidgetResponseDto projectWidget;
}
