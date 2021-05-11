package io.suricate.widget.tester.controllers.handlers;

import io.suricate.widget.tester.model.dto.error.ApiErrorDto;
import io.suricate.widget.tester.utils.exceptions.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manage Rest exceptions
 */
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * Logger
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(GlobalDefaultExceptionHandler.class);

    /**
     * Manage the API exception.
     *
     * @param exception The exception
     * @return The exception as Response Entity
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorDto> handleApiException(ApiException exception) {
      GlobalDefaultExceptionHandler.LOGGER.debug("An exception has occurred in the API controllers part", exception);

      return ResponseEntity
        .status(exception.getError().getStatus())
        .body(exception.getError());
    }
}
