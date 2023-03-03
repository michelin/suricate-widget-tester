package com.michelin.suricate.widget.tester.integrations;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class LibraryIntegrationTest {
    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldThrowNotFoundExceptionWhenLibraryNotFound() {
        ResponseEntity<ApiErrorDto> response = restTemplate.exchange("http://localhost:" + port + "/api/v1/libraries/doesNotExist/content?widgetPath=src/test/resources/repository/content/github/widgets/count-issues",
                GET, HttpEntity.EMPTY, ApiErrorDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("LibraryDto 'doesNotExist' not found");
    }

    @Test
    void shouldGetLibrary() {
        ResponseEntity<byte[]> response = restTemplate.exchange("http://localhost:" + port + "/api/v1/libraries/test.js/content?widgetPath=src/test/resources/repository/content/github/widgets/count-issues",
                GET, HttpEntity.EMPTY, byte[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
