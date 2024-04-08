package com.michelin.suricate.widget.tester.model.dto.config;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Configuration DTO.
 */
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ConfigurationDto extends AbstractDto {
    private String repository;
}
