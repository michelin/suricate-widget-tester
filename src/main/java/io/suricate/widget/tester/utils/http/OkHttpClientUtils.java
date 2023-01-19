package io.suricate.widget.tester.utils.http;

import lombok.extern.slf4j.Slf4j;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
public final class OkHttpClientUtils {
    /**
     * Read timeout
     */
    private static final int READ_TIMEOUT = 300;

    /**
     * Write timeout
     */
    private static final int WRITE_TIMEOUT = 300;

    /**
     * Connect timeout
     */
    private static final int CONNECT_TIMEOUT = 300;

    /**
     * Private constructor
     */
    private OkHttpClientUtils() { }

    /**
     * Get an instance of OkHttpClient without certificates validation
     *
     * @return An OkHttpClient instance
     */
    public static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificates chain
            final TrustManager[] trustManager = new TrustManager[] {
                    new AllTrustingTrustManager()
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustManager, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustManager[0])
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .retryOnConnectionFailure(true)
                    .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
                    .hostnameVerifier((s, sslSession) -> true);

            return builder.build();
        } catch (NoSuchAlgorithmException e) {
            log.error("An error occurred during the OKHttpClient configuration: SSL algorithm not found", e);
        } catch (KeyManagementException e) {
            log.error("An error occurred during the OKHttpClient configuration: Cannot init the SSL context", e);
        }

        return null;
    }
}
