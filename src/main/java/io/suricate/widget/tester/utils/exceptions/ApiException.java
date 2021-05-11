package io.suricate.widget.tester.utils.exceptions;

import io.suricate.widget.tester.model.dto.error.ApiErrorDto;
import io.suricate.widget.tester.model.enums.ApiErrorEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * API Exception management
 */
public class ApiException extends RuntimeException {

  /**
   * API error
   */
  private final ApiErrorEnum error;

  /**
   * Default constructor using field
   *
   * @param message custom message
   * @param error   the API error object to store into the exception
   */
  public ApiException(String message, ApiErrorEnum error) {
    super(StringUtils.isBlank(message) ? error.getMessage() : message);
    this.error = error;
  }

  /**
   * Method used to retrieve the error
   *
   * @return the APi error
   */
  public ApiErrorDto getError() {
    return error.toResponse(getMessage());
  }
}
