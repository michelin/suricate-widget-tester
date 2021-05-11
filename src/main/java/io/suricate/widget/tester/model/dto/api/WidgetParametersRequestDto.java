package io.suricate.widget.tester.model.dto.api;

import io.suricate.widget.tester.model.dto.AbstractDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Widget parameter request DTO, received from Front-End
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WidgetParametersRequestDto extends AbstractDto {

  /**
   * Widget parameter name
   */
  private String name;

  /**
   * Widget parameter value
   */
  private String value;
}
