package io.suricate.widget.tester.services.nashorn.script;

import io.suricate.widget.tester.utils.exceptions.nashorn.FatalException;
import io.suricate.widget.tester.utils.exceptions.nashorn.RemoteException;
import io.suricate.widget.tester.utils.exceptions.nashorn.RequestException;
import io.suricate.widget.tester.utils.http.OkHttpClientUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

public final class NashornWidgetScript {

    /**
     * Default httpClient
     */
    private static OkHttpClient client = OkHttpClientUtils.getUnsafeOkHttpClient();

    /**
     * private constructor
     */
    private NashornWidgetScript() { }

    /**
     * Create and submit a HTTP request according to the given parameters
     *
     * @param url The URL of the endpoint to call
     * @param headerName The name of the header to add
     * @param headerValue The value to set to the added header
     * @param headerToReturn The name of the header to return
     * @param body The body of the request. Can be null in case of GET HTTP request
     * @param mediaType The requested media type
     * @return The response body of the request or the value of the requested header
     * @throws IOException
     * @throws RemoteException
     * @throws RequestException
     */
    private static String executeRequest(String url, String headerName, String headerValue, String headerToReturn, String body, String mediaType)
            throws IOException, RemoteException, RequestException {
        Request.Builder builder = new Request.Builder().url(url);

        if (StringUtils.isNotBlank(headerName)) {
            builder.addHeader(headerName, headerValue);
        }

        if (StringUtils.isNotBlank(body)) {
            builder.post(RequestBody.create(body, MediaType.parse(mediaType)));
        }

        Request request = builder.build();
        String returnedValue = null;

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                if (StringUtils.isNotBlank(headerToReturn)) {
                    returnedValue = response.header(headerToReturn);
                } else {
                    returnedValue = Objects.requireNonNull(response.body()).string();
                }
            } else {
                if (response.code() >= HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                    throw new RemoteException("Response error: " + response.message() + " code:" + response.code());
                } else {
                    throw new RequestException(
                            response.message() + " - code:" + response.code(),
                            response.body() != null ? response.body().string() : null
                    );
                }
            }
        }

        return returnedValue;
    }

    /**
     * Perform a GET HTTP call
     * This method is directly called by the widgets
     *
     * @param url The URL of the endpoint to call
     * @return The response body of the request
     * @throws RemoteException
     * @throws IOException
     * @throws RequestException
     */
    public static String get(String url) throws RemoteException, IOException, RequestException {
        return NashornWidgetScript.executeRequest(url, null, null, null, null, "application/json");
    }

    /**
     * Perform a GET HTTP call
     * This method is directly called by the widgets
     *
     * Accept the name of a header as parameter and a value to set it to
     * add them to the request
     *
     * @param url The URL of the endpoint to call
     * @param headerName The name of the header to add
     * @param headerValue The value to set to the added header
     * @return The response body of the request
     * @throws RemoteException
     * @throws IOException
     * @throws RequestException
     */
    public static String get(String url, String headerName, String headerValue) throws RemoteException, IOException, RequestException {
        return NashornWidgetScript.executeRequest(url, headerName, headerValue, null, null, "application/json");
    }

    /**
     * Perform a GET HTTP call
     * This method is directly called by the widgets
     *
     * Accept the name of a header as parameter and a value to set it to
     * add them to the request
     *
     * Accept the name of a header as parameter to return its value
     *
     * @param url The URL of the endpoint to call
     * @param headerName The name of the header to add
     * @param headerValue The value to set to the added header
     * @param headerToReturn The name of the header to return
     * @return The requested header
     * @throws RemoteException
     * @throws IOException
     * @throws RequestException
     */
    public static String get(String url, String headerName, String headerValue, String headerToReturn) throws RemoteException, IOException, RequestException {
        return NashornWidgetScript.executeRequest(url, headerName, headerValue, headerToReturn, null, "application/json");
    }

    /**
     * Perform a POST HTTP call
     * This method is directly called by the widgets
     *
     * @param url The URL of the endpoint to call
     * @param body The body of the POST request
     * @return The response body of the request
     * @throws RemoteException
     * @throws IOException
     * @throws RequestException
     */
    public static String post(String url, String body) throws RemoteException, IOException, RequestException {
        return NashornWidgetScript.executeRequest(url, null, null, null, StringUtils.isBlank(body) ? "{}" : body, "application/json");
    }

    /**
     * Perform a POST HTTP call
     * This method is directly called by the widgets
     *
     * Accept the name of a header as parameter and a value to set it to
     * add them to the request
     *
     * @param url The URL of the endpoint to call
     * @param body The body of the POST request
     * @param headerName The name of the header to add
     * @param headerValue The value to set to the added header
     * @return The response body of the request
     * @throws RemoteException
     * @throws IOException
     * @throws RequestException
     */
    public static String post(String url, String body, String headerName, String headerValue) throws RemoteException, IOException, RequestException {
        return NashornWidgetScript.executeRequest(url, headerName, headerValue, null, StringUtils.isBlank(body) ? "{}" : body, "application/json");
    }

    /**
     * Perform a POST HTTP call
     * This method is directly called by the widgets
     *
     * Accept the name of a header as parameter and a value to set it to
     * add them to the request
     *
     * Accept a given media type
     *
     * @param url The URL of the endpoint to call
     * @param body The body of the POST request
     * @param headerName The name of the header to add
     * @param headerValue The value to set to the added header
     * @param mediaType The requested media type
     * @return The response body of the request
     * @throws RemoteException
     * @throws IOException
     * @throws RequestException
     */
    public static String post(String url, String body, String headerName, String headerValue, String mediaType) throws RemoteException, IOException, RequestException {
        return NashornWidgetScript.executeRequest(url, headerName, headerValue, null, StringUtils.isBlank(body) ? "{}" : body, mediaType);
    }

    /**
     * Check if a thread is interrupted
     * This method is injected during the Nashorn request preparation
     *
     * @throws InterruptedException an exception if the thread is interrupted
     */
    public static void checkInterrupted() throws InterruptedException {
        if (Thread.currentThread().isInterrupted()){
            throw new InterruptedException("Script Interrupted");
        }
    }

    /**
     * Convert ASCII string to base 64
     * This method is directly called by the widgets especially to encrypt credentials
     *
     * @param data The string to convert
     * @return A String encoded with Base64
     */
    public static String btoa(String data) {
        if (StringUtils.isBlank(data)) {
            return null;
        }
        return Base64.getEncoder().encodeToString(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Throw a remote error
     *
     * @throws RemoteException The error thrown
     */
    public static void throwError() throws RemoteException {
        throw new RemoteException("Error");
    }

    /**
     * Throw a fatal error
     *
     * @throws FatalException The error thrown
     */
    public static void throwFatalError(String msg) throws FatalException {
        throw new FatalException(msg);
    }

    /**
     * Throw a timeout exception
     *
     * @throws TimeoutException The error thrown
     */
    public static void throwTimeout() throws TimeoutException {
        throw new TimeoutException("Timeout");
    }
}

