package io.suricate.widget.tester.model.dto.api;

import io.suricate.widget.tester.model.dto.AbstractDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Project widget response DTO
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProjectWidgetResponseDto extends AbstractDto {

    /**
     * The instantiation of the html widget template
     */
    private String instantiateHtml;

    /**
     * Widget technical name
     */
    private String technicalName;

    /**
     * CSS content
     */
    private String cssContent;

    /**
     * The log of the last nashorn execution
     */
    private String log;

    /**
     * Names of the libraries used by the widget
     */
    private List<String> librariesNames;
}
