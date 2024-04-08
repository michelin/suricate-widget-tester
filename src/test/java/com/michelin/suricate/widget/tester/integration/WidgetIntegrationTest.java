package com.michelin.suricate.widget.tester.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.GET;

import com.michelin.suricate.widget.tester.model.dto.error.ApiErrorDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetParamDto;
import java.io.File;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WidgetIntegrationTest {
    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldGetWidgetParams() {
        ResponseEntity<List<WidgetParamDto>> response = restTemplate.exchange("http://localhost:" + port
                + "/api/v1/widgets/parameters?category=github&widget=count-issues",
            GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().size());
        assertIterableEquals(List.of("SURI_GITHUB_ORG", "SURI_GITHUB_PROJECT", "SURI_ISSUES_STATE",
            "WIDGET_CONFIG_GITHUB_TOKEN"),
            response.getBody().stream().map(WidgetParamDto::getName).toList());
    }

    @Test
    void shouldNotGetWidgetParamsWhenCategoryDoesNotExist() {
        ResponseEntity<ApiErrorDto> response = restTemplate.exchange("http://localhost:" + port
                + "/api/v1/widgets/parameters?category=unknown&widget=count-issues",
            GET, HttpEntity.EMPTY, ApiErrorDto.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());

        String expectedFileName = "src"
            + File.separator + "test" + File.separator + "resources" + File.separator + "repository"
            + File.separator + "content" + File.separator + "unknown";

        assertTrue(response.getBody().getMessage().contains("The file"));
        assertTrue(response.getBody().getMessage().contains(expectedFileName));
        assertTrue(response.getBody().getMessage().contains("does not exist"));
    }
}
