package com.michelin.suricate.widget.tester.controller;

import com.michelin.suricate.widget.tester.model.dto.config.ConfigurationDto;
import com.michelin.suricate.widget.tester.property.ApplicationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Library controller.
 */
@RestController
@RequestMapping("/api")
public class ConfigurationController {
    @Autowired
    private ApplicationProperties applicationProperties;

    @GetMapping(value = "/v1/configurations/repository")
    public ResponseEntity<ConfigurationDto> getRepository() {
        return ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(ConfigurationDto.builder()
                .repository(applicationProperties.getWidgets().getRepository())
                .build());
    }
}
