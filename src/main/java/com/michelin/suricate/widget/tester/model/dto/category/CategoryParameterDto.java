package com.michelin.suricate.widget.tester.model.dto.category;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import com.michelin.suricate.widget.tester.model.enums.DataTypeEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Category parameter DTO
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CategoryParameterDto extends AbstractDto {

  /**
   * The key of the configuration (used in JS Files)
   */
  private String key;

  /**
   * The related value enter by the user
   */
  private String value;

  /**
   * export
   */
  private boolean export;

  /**
   * The data type of the configuration
   */
  private DataTypeEnum dataType;
}
