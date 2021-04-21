package io.suricate.widget.tester.backend.utils.http;

import io.suricate.widget.tester.backend.properties.ProxyConfiguration;
import io.suricate.widget.tester.backend.utils.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

/**
 * Custom proxy selector
 */
public class WidgetProxySelector extends ProxySelector {

    /**
     * Class logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WidgetProxySelector.class);

    /**
     * Select the proxy settings from the application configuration for the given URI.
     * If the URI is defined is the no proxy domains configuration, then no proxy is applied.
     *
     * @param uri The URI to check if it needs a proxy or not
     * @return A list of proxies
     */
    @Override
    public List<Proxy> select(URI uri) {
        Proxy proxy = Proxy.NO_PROXY;
        ProxyConfiguration proxyConfiguration = SpringContextUtils.getApplicationContext().getBean(ProxyConfiguration.class);

        if (StringUtils.isNotBlank(proxyConfiguration.getNoProxyDomains())) {
            try (Stream<String> stream = Arrays.stream(proxyConfiguration.getNoProxyDomains().split(","))) {
                if (StringUtils.isNotBlank(proxyConfiguration.getNoProxyDomains()) &&
                        stream.noneMatch(h -> StringUtils.containsIgnoreCase(uri.getHost(), h))) {
                    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyConfiguration.getHost(), Integer.parseInt(proxyConfiguration.getPort())));
                }
            }
        }

        return Collections.singletonList(proxy);
    }

    /**
     * Handle the connection fails to the given proxy
     *
     * @param uri The URI to call behind the proxy
     * @param socketAddress The socket address
     * @param e The exception
     */
    @Override
    public void connectFailed(URI uri, SocketAddress socketAddress, IOException e) {
        LOGGER.error("An error occurred trying to connect to the configured proxy for the URI {} during the widget execution", uri, e);
    }
}
