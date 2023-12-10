package com.michelin.suricate.widget.tester.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * Json utils.
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonUtils {
    /**
     * Validate the JSON parameter.
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
                log.trace(e.getMessage(), e);
            }
        }

        return false;
    }

    /**
     * Prettify JSON.
     *
     * @param uglyJson The JSON to prettify
     * @return The prettified JSON
     */
    public static String prettifyJson(String uglyJson) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(JsonParser.parseString(uglyJson));
    }
}

