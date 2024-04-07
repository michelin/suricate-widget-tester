package com.michelin.suricate.widget.tester.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Application properties.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {
    private CorsConfiguration cors;
    private Widgets widgets;

    /**
     * Widgets properties.
     */
    @Getter
    @Setter
    public static class Widgets {
        private String repository = "/tmp";
    }
}
