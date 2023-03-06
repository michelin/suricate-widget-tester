package com.michelin.suricate.widget.tester.services.nashorn.services;

import com.michelin.suricate.widget.tester.model.dto.nashorn.NashornRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class NashornServiceTest {
    @InjectMocks
    private NashornService nashornService;

    @Test
    void shouldNashornRequestNotExecutableBecauseNoScript() {
        NashornRequest nashornRequest = new NashornRequest();
        nashornRequest.setProjectId(1L);

        boolean actual = nashornService.isNashornRequestExecutable(nashornRequest);

        assertThat(actual).isFalse();
    }

    @Test
    void shouldNashornRequestNotExecutableBecauseInvalidData() {
        NashornRequest nashornRequest = new NashornRequest();
        nashornRequest.setProjectId(1L);
        nashornRequest.setScript("script");
        nashornRequest.setPreviousData("invalid");

        boolean actual = nashornService.isNashornRequestExecutable(nashornRequest);

        assertThat(actual).isFalse();
    }

    @Test
    void shouldNashornRequestNotExecutableBecauseNoDelay() {
        NashornRequest nashornRequest = new NashornRequest();
        nashornRequest.setProjectId(1L);
        nashornRequest.setScript("script");
        nashornRequest.setPreviousData("{\"key\": \"value\"}");

        boolean actual = nashornService.isNashornRequestExecutable(nashornRequest);

        assertThat(actual).isFalse();
    }

    @Test
    void shouldNashornRequestNotExecutableBecauseDelayLowerThan0() {
        NashornRequest nashornRequest = new NashornRequest();
        nashornRequest.setProjectId(1L);
        nashornRequest.setScript("script");
        nashornRequest.setPreviousData("{\"key\": \"value\"}");
        nashornRequest.setDelay(-10L);

        boolean actual = nashornService.isNashornRequestExecutable(nashornRequest);

        assertThat(actual).isFalse();
    }

    @Test
    void shouldNashornRequestBeExecutable() {
        NashornRequest nashornRequest = new NashornRequest();
        nashornRequest.setProjectId(1L);
        nashornRequest.setScript("script");
        nashornRequest.setPreviousData("{\"key\": \"value\"}");
        nashornRequest.setDelay(10L);

        boolean actual = nashornService.isNashornRequestExecutable(nashornRequest);

        assertThat(actual).isTrue();
    }
}
