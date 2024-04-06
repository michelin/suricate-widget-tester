package com.michelin.suricate.widget.tester.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import com.michelin.suricate.widget.tester.model.dto.config.ConfigurationDto;
import com.michelin.suricate.widget.tester.property.ApplicationProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class ConfigurationControllerTest {
    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private ConfigurationController configurationController;

    @Test
    void shouldGetRepository() {
        when(applicationProperties.getWidgets())
            .thenReturn(new ApplicationProperties.Widgets());

        ResponseEntity<ConfigurationDto> actual = configurationController.getRepository();

        assertEquals(HttpStatus.OK, actual.getStatusCode());
        assertNotNull(actual.getBody());
        assertEquals("/tmp", actual.getBody().getRepository());
    }
}
