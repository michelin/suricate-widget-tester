package com.michelin.suricate.widget.tester.integrations;

import com.michelin.suricate.widget.tester.model.dto.error.ApiErrorDto;
import com.michelin.suricate.widget.tester.model.dto.widget.WidgetParamDto;
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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpMethod.GET;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class WidgetIntegrationTest {
    @Value(value="${local.server.port}")
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldGetWidgetParams() {
        ResponseEntity<List<WidgetParamDto>> response = restTemplate.exchange("http://localhost:" + port + "/api/v1/parameters?widgetPath=src/test/resources/repository/content/github/widgets/count-issues",
                GET, HttpEntity.EMPTY, new ParameterizedTypeReference<List<WidgetParamDto>>() {});

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(4);
        assertThat(response.getBody().stream().map(WidgetParamDto::getName)).containsAll(Arrays.asList("SURI_GITHUB_ORG", "SURI_GITHUB_PROJECT", "SURI_ISSUES_STATE",
                "WIDGET_CONFIG_GITHUB_TOKEN"));
    }

    @Test
    void shouldNotGetWidgetParamsWhenFileDoesNotExist() {
        ResponseEntity<ApiErrorDto> response = restTemplate.exchange("http://localhost:" + port + "/api/v1/parameters?widgetPath=src/test/resources/error/repository/content/github/widgets/count-issues",
                GET, HttpEntity.EMPTY, ApiErrorDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("The file src\\test\\resources\\error\\repository\\content\\github\\widgets\\count-issues does not exist");
    }
}
