package com.michelin.suricate.widget.tester.model.dto.widget;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import com.michelin.suricate.widget.tester.model.enums.WidgetAvailabilityEnum;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Widget DTO.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WidgetDto extends AbstractDto {
    private Long id;
    private String name;
    private String description;
    private String technicalName;
    private String htmlContent;
    private String cssContent;
    private String backendJs;
    private String info;
    private Long delay;
    private Long timeout;
    private WidgetAvailabilityEnum widgetAvailability;
    private List<WidgetParamDto> widgetParams = new ArrayList<>();
    private byte[] image;
    private String[] libraries;
}
