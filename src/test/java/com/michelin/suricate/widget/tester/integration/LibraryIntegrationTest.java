package com.michelin.suricate.widget.tester.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.GET;

import com.michelin.suricate.widget.tester.model.dto.error.ApiErrorDto;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LibraryIntegrationTest {
    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldThrowNotFoundExceptionWhenLibraryNotFound() {
        ResponseEntity<ApiErrorDto> response = restTemplate.exchange("http://localhost:" + port
                + "/api/v1/libraries/doesNotExist/content",
            GET, HttpEntity.EMPTY, ApiErrorDto.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("LibraryDto 'doesNotExist' not found", response.getBody().getMessage());
    }

    @Test
    void shouldGetLibrary() {
        ResponseEntity<byte[]> response = restTemplate.exchange("http://localhost:" + port
                + "/api/v1/libraries/test.js/content",
            GET, HttpEntity.EMPTY, byte[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
