package io.suricate.widget.tester.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class JsonUtils {

    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonUtils.class);

    /**
     * Constructor
     */
    private JsonUtils() { }

    /**
     * Validate the JSON parameter
     *
     * @param jsonInString the json string to test
     * @return true if the json is isValid false otherwise
     */
    public static boolean isValid(String jsonInString) {
        if (StringUtils.isNotBlank(jsonInString)) {
            try {
                final ObjectMapper mapper = new ObjectMapper();
                mapper.readTree(jsonInString);
                return true;
            } catch (IOException e) {
                // do nothing
                LOGGER.trace(e.getMessage(), e);
            }
        }

        return false;
    }

    /**
     * Prettify JSON
     *
     * @param uglyJson The JSON to prettify
     * @return The prettified JSON
     */
    public static String prettifyJson(String uglyJson) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      JsonParser jsonParser = new JsonParser();
      JsonElement jsonElement = jsonParser.parse(uglyJson);
      return gson.toJson(jsonElement);
    }
}

