package com.michelin.suricate.widget.tester.utils.http;

import java.security.cert.X509Certificate;
import javax.net.ssl.X509TrustManager;

/**
 * Trust manager that does not check certificates.
 */
public class AllTrustingTrustManager implements X509TrustManager {
    /**
     * Do not check the trusted client.
     *
     * @param x509Certificates The certificates
     * @param s                The string
     */
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        // Do not check the trusted client
    }

    /**
     * Do not check certificates.
     *
     * @param x509Certificates The certificates
     * @param s                The string
     */
    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        // Do not check certificates
    }

    /**
     * Do not get issuers.
     *
     * @return An empty list of issuers
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[] {};
    }
}
