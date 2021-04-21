package io.suricate.widget.tester.backend.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

public final class PropertiesUtils {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    /**
     * Constructor
     */
    private PropertiesUtils() { }

    /**
     * Convert widget parameters values from string to map
     *
     * @param widgetProperties the string containing the widget parameters values (key1=value1)
     * @return The widget parameters values as map
     */
    public static Map<String, String> convertStringWidgetPropertiesToMap(String widgetProperties) {
        Map<String, String> mappedWidgetProperties = new TreeMap<>();
        Properties properties = convertStringWidgetPropertiesToProperties(widgetProperties);

        if (properties != null) {
            for (String propertyName : properties.stringPropertyNames()) {
                if (!properties.getProperty(propertyName).trim().isEmpty()) {
                    mappedWidgetProperties.put(propertyName, properties.getProperty(propertyName));
                }
            }
        }

        return mappedWidgetProperties;
    }

    /**
     * Convert widget parameters values from string to Properties
     *
     * @param widgetProperties the string containing the widget parameters values
     * @return The widget parameters values as Properties
     */
    public static Properties convertStringWidgetPropertiesToProperties(String widgetProperties) {
        Properties properties = null;

        if (StringUtils.isNotBlank(widgetProperties)) {
            try (StringReader reader = new StringReader(widgetProperties)){
                properties = new Properties();
                properties.load(reader);
            } catch (IOException e) {
                LOGGER.error("An error has occurred converting widget parameters values from string to Properties: {}", widgetProperties, e);
            }
        }

        return properties;
    }
}
