package io.suricate.widget.tester.backend.model.dto.widget;

import io.suricate.widget.tester.backend.model.dto.AbstractDto;
import io.suricate.widget.tester.backend.model.enums.WidgetAvailabilityEnum;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Widget DTO
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class WidgetDto extends AbstractDto {

    /**
     * The widget ID
     */
    private Long id;

    /**
     * The widget name
     */
    private String name;

    /**
     * The widget description
     */
    private String description;

    /**
     * The technical name
     */
    private String technicalName;

    /**
     * The html content of the widget
     */
    private String htmlContent;

    /**
     * The css content of the widget
     */
    private String cssContent;

    /**
     * The JS of the widget (executed by nashorn
     */
    private String backendJs;

    /**
     * Information on the usage of this widget
     */
    private String info;

    /**
     * The delay for each execution of this widget
     */
    private Long delay;

    /**
     * The timeout of the nashorn execution
     */
    private Long timeout;

    /**
     * The widget availability {@link WidgetAvailabilityEnum}
     */
    private WidgetAvailabilityEnum widgetAvailability;

    /**
     * The list of the params for this widget
     */
    private List<WidgetParamDto> widgetParams = new ArrayList<>();

    /**
     * The widget's image
     */
    private byte[] image;

    /**
     * The widget's libraries names
     */
    private String[] libraries;
}
