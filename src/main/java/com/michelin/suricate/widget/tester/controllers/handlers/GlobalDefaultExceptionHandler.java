package com.michelin.suricate.widget.tester.controllers.handlers;

import com.michelin.suricate.widget.tester.model.dto.error.ApiErrorDto;
import com.michelin.suricate.widget.tester.model.enums.ApiErrorEnum;
import com.michelin.suricate.widget.tester.utils.exceptions.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.NoSuchFileException;

@Slf4j
@RestControllerAdvice
public class GlobalDefaultExceptionHandler {

    /**
     * Manage the API exception.
     *
     * @param exception The exception
     * @return The exception as Response Entity
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorDto> handleApiException(ApiException exception) {
      log.debug("An exception has occurred in the API controllers part", exception);

      return ResponseEntity
        .status(exception.getError().getStatus())
        .body(exception.getError());
    }

    /**
     * Manage the API exception.
     *
     * @param exception The exception
     * @return The exception as Response Entity
     */
    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<ApiErrorDto> handleApiException(NoSuchFileException exception) {
        log.debug("An exception has occurred in the API controllers part", exception);

        return ResponseEntity
                .status(ApiErrorEnum.FILE_ERROR.getStatus())
                .body(new ApiErrorDto(String.format("The file %s does not exist", exception.getMessage()), ApiErrorEnum.FILE_ERROR));
    }
}
