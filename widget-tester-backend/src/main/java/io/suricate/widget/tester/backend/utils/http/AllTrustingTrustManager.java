package io.suricate.widget.tester.backend.utils.http;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class AllTrustingTrustManager implements X509TrustManager {

    /**
     * Do not check the trusted client
     *
     * @param x509Certificates
     * @param s
     */
    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        // Do not check the trusted client
    }

    /**
     * Do not check certificates
     *
     * @param x509Certificates
     * @param s
     */
    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        // Do not check certificates
    }

    /**
     * Do not get issuers
     *
     * @return An empty list of issuers
     */
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[]{};
    }
}
