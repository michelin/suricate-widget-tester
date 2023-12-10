package com.michelin.suricate.widget.tester.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * Abstract DTO.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractDto implements Serializable {

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
