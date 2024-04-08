package com.michelin.suricate.widget.tester.util.exception;

import com.michelin.suricate.widget.tester.model.dto.error.ApiErrorDto;
import com.michelin.suricate.widget.tester.model.enumeration.ApiErrorEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * Api exception.
 */
public class ApiException extends RuntimeException {
    private final ApiErrorEnum error;

    /**
     * Constructor.
     *
     * @param message custom message
     * @param error   the API error object to store into the exception
     */
    public ApiException(String message, ApiErrorEnum error) {
        super(StringUtils.isBlank(message) ? error.getMessage() : message);
        this.error = error;
    }

    /**
     * Method used to retrieve the error.
     *
     * @return the API error
     */
    public ApiErrorDto getError() {
        return error.toResponse(getMessage());
    }
}
