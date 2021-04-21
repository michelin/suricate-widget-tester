package io.suricate.widget.tester.backend.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Manage the proxy configuration
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "proxy")
public class ProxyConfiguration {

    /**
     * Proxy host
     */
    private String host;

    /**
     * Proxy port
     */
    private String port;

    /**
     * List of all proxy domain to ignore
     */
    private String noProxyDomains;

    /**
     * Set JVM settings for http proxy
     */
    public void setProxy() {
        if (!StringUtils.isAllEmpty(host, port) && StringUtils.isNumeric(port)) {
            System.setProperty("http.proxyHost", host);
            System.setProperty("http.proxyPort", port);

            if (!StringUtils.isAllEmpty(noProxyDomains)) {
                System.setProperty("http.nonProxyHosts", noProxyDomains);
            }
        }
    }
}

