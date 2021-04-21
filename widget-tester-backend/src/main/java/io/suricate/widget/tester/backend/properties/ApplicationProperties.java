package io.suricate.widget.tester.backend.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "widget")
public class ApplicationProperties {

    private String folder;

    private final Map<String, String> parameters = new HashMap<>();

    private String previousData;
}
