package io.suricate.widget.tester.backend.utils.exceptions.nashorn;

import lombok.Getter;

public class RequestException extends Exception {

    /**
     * Technical message of the request exception
     */
    @Getter
    private final String technicalData;

    /**
     * Response body of the request exception
     */
    @Getter
    private final String response;

    /**
     * Constructor
     *
     * @param technicalData The technical data
     * @param response The response body
     */
    public RequestException(String technicalData, String response) {
        super(technicalData);
        this.technicalData = technicalData;
        this.response = response;
    }
}
