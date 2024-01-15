package com.michelin.suricate.widget.tester.configuration.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mustache configuration.
 */
@Configuration
public class MustacheConfiguration {
    /**
     * Default mustache factory.
     */
    @Bean
    protected MustacheFactory mustacheFactory() {
        return new DefaultMustacheFactory();
    }
}
