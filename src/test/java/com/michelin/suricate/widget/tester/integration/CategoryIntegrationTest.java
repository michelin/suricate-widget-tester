package com.michelin.suricate.widget.tester.integration;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.http.HttpMethod.GET;

import com.michelin.suricate.widget.tester.model.dto.category.CategoryDirectoryDto;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CategoryIntegrationTest {
    @Value(value = "${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldGetCategoryDirectories() {
        ResponseEntity<List<CategoryDirectoryDto>> response = restTemplate.exchange("http://localhost:" + port
                + "/api/v1/categories/directories",
            GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {});

        assertNotNull(response.getBody());
        assertIterableEquals(List.of("github", "gitlab", "other"), response.getBody().stream()
            .map(CategoryDirectoryDto::getName)
            .toList());

        Optional<CategoryDirectoryDto> github = response.getBody()
            .stream()
            .filter(category -> category.getName().equals("github"))
            .findFirst();

        assertTrue(github.isPresent());
        assertIterableEquals(List.of("count-issues"), github.get().getWidgets());
    }
}
