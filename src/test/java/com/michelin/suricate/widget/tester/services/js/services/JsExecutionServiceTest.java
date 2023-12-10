package com.michelin.suricate.widget.tester.services.js.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.michelin.suricate.widget.tester.model.dto.js.JsExecutionDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JsExecutionServiceTest {
    @InjectMocks
    private JsExecutionService jsExecutionService;

    @Test
    void shouldNashornRequestNotExecutableBecauseNoScript() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);

        boolean actual = jsExecutionService.isJsExecutable(jsExecutionDto);

        assertThat(actual).isFalse();
    }

    @Test
    void shouldNashornRequestNotExecutableBecauseInvalidData() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setScript("script");
        jsExecutionDto.setPreviousData("invalid");

        boolean actual = jsExecutionService.isJsExecutable(jsExecutionDto);

        assertThat(actual).isFalse();
    }

    @Test
    void shouldNashornRequestNotExecutableBecauseNoDelay() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setScript("script");
        jsExecutionDto.setPreviousData("{\"key\": \"value\"}");

        boolean actual = jsExecutionService.isJsExecutable(jsExecutionDto);

        assertThat(actual).isFalse();
    }

    @Test
    void shouldNashornRequestNotExecutableBecauseDelayLowerThan0() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setScript("script");
        jsExecutionDto.setPreviousData("{\"key\": \"value\"}");
        jsExecutionDto.setDelay(-10L);

        boolean actual = jsExecutionService.isJsExecutable(jsExecutionDto);

        assertThat(actual).isFalse();
    }

    @Test
    void shouldNashornRequestBeExecutable() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setScript("script");
        jsExecutionDto.setPreviousData("{\"key\": \"value\"}");
        jsExecutionDto.setDelay(10L);

        boolean actual = jsExecutionService.isJsExecutable(jsExecutionDto);

        assertThat(actual).isTrue();
    }
}
