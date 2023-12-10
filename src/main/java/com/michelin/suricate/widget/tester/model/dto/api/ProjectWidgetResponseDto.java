package com.michelin.suricate.widget.tester.model.dto.api;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Project widget response DTO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectWidgetResponseDto extends AbstractDto {
    private String instantiateHtml;
    private String technicalName;
    private String cssContent;
    private String log;
    private List<String> librariesNames;
}
