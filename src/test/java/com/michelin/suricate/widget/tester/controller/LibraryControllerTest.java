package com.michelin.suricate.widget.tester.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import com.michelin.suricate.widget.tester.model.dto.library.LibraryDto;
import com.michelin.suricate.widget.tester.property.ApplicationProperties;
import com.michelin.suricate.widget.tester.util.WidgetUtils;
import com.michelin.suricate.widget.tester.util.exception.ObjectNotFoundException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class LibraryControllerTest {
    @Mock
    private ApplicationProperties applicationProperties;

    @InjectMocks
    private LibraryController libraryController;
    
    @Test
    void shouldGetLibrary() {
        when(applicationProperties.getWidgets())
            .thenReturn(new ApplicationProperties.Widgets());

        try (MockedStatic<WidgetUtils> mocked = mockStatic(WidgetUtils.class)) {
            mocked.when(() -> WidgetUtils.parseLibraryFolder(any()))
                .thenReturn(List.of(LibraryDto.builder()
                    .technicalName("libraryName")
                    .asset(new byte[10])
                    .build()));

            ResponseEntity<byte[]> actual = libraryController.getLibrary("libraryName");

            assertEquals(HttpStatus.OK, actual.getStatusCode());
            assertNotNull(actual.getBody());
            assertEquals(10, actual.getBody().length);
        }
    }

    @Test
    void shouldThrowObjectNotFoundException() {
        when(applicationProperties.getWidgets())
            .thenReturn(new ApplicationProperties.Widgets());

        try (MockedStatic<WidgetUtils> mocked = mockStatic(WidgetUtils.class)) {
            mocked.when(() -> WidgetUtils.parseLibraryFolder(any()))
                .thenReturn(List.of(LibraryDto.builder()
                    .technicalName("libraryName")
                    .asset(new byte[10])
                    .build()));

            ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> libraryController.getLibrary("unknownLibraryName"));

            assertEquals("LibraryDto 'unknownLibraryName' not found", exception.getMessage());
        }
    }
}
