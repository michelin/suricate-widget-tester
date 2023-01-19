package io.suricate.widget.tester.utils.exceptions.nashorn;

import lombok.Getter;

public class RequestException extends Exception {
    public RequestException(String message) {
        super(message);
    }
}
