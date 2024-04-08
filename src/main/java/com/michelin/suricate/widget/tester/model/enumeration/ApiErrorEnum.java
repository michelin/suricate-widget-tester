package com.michelin.suricate.widget.tester.model.enumeration;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.michelin.suricate.widget.tester.model.dto.error.ApiErrorDto;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Enum used to define the error type.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ApiErrorEnum {
    NO_CONTENT("No Content", "no.content", HttpStatus.NO_CONTENT),
    BAD_REQUEST("Bad Request", "bad.request", HttpStatus.BAD_REQUEST),
    PROJECT_TOKEN_INVALID("Cannot decrypt project token", "project.token.invalid", HttpStatus.BAD_REQUEST),
    AUTHENTICATION_ERROR("Authentication error, token expired or invalid", "authentication.error",
        HttpStatus.UNAUTHORIZED),
    NOT_AUTHORIZED("User not authorized", "not.authorized", HttpStatus.UNAUTHORIZED),
    BAD_CREDENTIALS_ERROR("Bad credentials", "authentication.bad.credentials", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("You don't have permission to access to this resource", "user.forbidden", HttpStatus.FORBIDDEN),
    FILE_ERROR("File cannot be read", "file.cannot.read", HttpStatus.INTERNAL_SERVER_ERROR),
    OBJECT_NOT_FOUND("Object not found", "object.not.found", HttpStatus.NOT_FOUND),
    OBJECT_ALREADY_EXIST("Object already exist", "object.already.exist", HttpStatus.CONFLICT),
    PRECONDITION_FAILED("Precondition failed for this request", "precondition.failed", HttpStatus.PRECONDITION_FAILED),
    INTERNAL_SERVER_ERROR("Internal Server Error", "internal.server.error", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String message;
    private final int ordinal;
    private final String key;
    private final HttpStatus status;

    /**
     * Constructor.
     *
     * @param message The message
     * @param key     The key
     * @param status  The HttpStatus
     */
    ApiErrorEnum(String message, String key, HttpStatus status) {
        this.status = status;
        this.message = message;
        this.ordinal = ordinal();
        this.key = key;
    }

    public ApiErrorDto toResponse(String message) {
        return new ApiErrorDto(message, this);
    }
}
