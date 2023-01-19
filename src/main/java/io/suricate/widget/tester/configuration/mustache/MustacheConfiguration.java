package io.suricate.widget.tester.configuration.mustache;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.MustacheFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MustacheConfiguration {

    @Bean
    protected MustacheFactory mustacheFactory() {
      return new DefaultMustacheFactory();
    }
}
