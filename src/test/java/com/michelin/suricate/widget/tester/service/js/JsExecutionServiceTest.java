package com.michelin.suricate.widget.tester.service.js;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void shouldJsExecutionNotExecutableBecauseNoScript() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);

        boolean actual = jsExecutionService.isJsExecutable(jsExecutionDto);
        assertFalse(actual);
    }

    @Test
    void shouldJsExecutionNotExecutableBecauseInvalidData() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setScript("script");
        jsExecutionDto.setPreviousData("invalid");

        boolean actual = jsExecutionService.isJsExecutable(jsExecutionDto);
        assertFalse(actual);
    }

    @Test
    void shouldJsExecutionNotExecutableBecauseNoDelay() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setScript("script");
        jsExecutionDto.setPreviousData("{\"key\": \"value\"}");

        boolean actual = jsExecutionService.isJsExecutable(jsExecutionDto);
        assertFalse(actual);
    }

    @Test
    void shouldJsExecutionNotExecutableBecauseDelayLowerThan0() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setScript("script");
        jsExecutionDto.setPreviousData("{\"key\": \"value\"}");
        jsExecutionDto.setDelay(-10L);

        boolean actual = jsExecutionService.isJsExecutable(jsExecutionDto);
        assertFalse(actual);
    }

    @Test
    void shouldJsExecutionBeExecutable() {
        JsExecutionDto jsExecutionDto = new JsExecutionDto();
        jsExecutionDto.setProjectId(1L);
        jsExecutionDto.setScript("script");
        jsExecutionDto.setPreviousData("{\"key\": \"value\"}");
        jsExecutionDto.setDelay(10L);

        boolean actual = jsExecutionService.isJsExecutable(jsExecutionDto);
        assertTrue(actual);
    }
}
