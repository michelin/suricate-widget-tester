package com.michelin.suricate.widget.tester.utils.exceptions.js;

/**
 * Exception thrown when a fatal error occurs during the execution of a widget script.
 */
public class FatalException extends Exception {
    /**
     * Constructor.
     *
     * @param message The message of the exception
     */
    public FatalException(String message) {
        super(message);
    }
}
