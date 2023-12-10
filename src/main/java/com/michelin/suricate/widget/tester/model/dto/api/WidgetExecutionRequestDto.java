package com.michelin.suricate.widget.tester.model.dto.api;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Widget execution request DTO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WidgetExecutionRequestDto extends AbstractDto {
    private String path;
    private String previousData = "{}";
    private List<WidgetParametersRequestDto> parameters = new ArrayList<>();
}
