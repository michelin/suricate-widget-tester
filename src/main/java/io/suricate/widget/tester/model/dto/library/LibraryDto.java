package io.suricate.widget.tester.model.dto.library;

import io.suricate.widget.tester.model.dto.AbstractDto;
import lombok.*;

/**
 * Library DTO
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LibraryDto extends AbstractDto {

    /**
     * The library ID
     */
    private Long id;

    /**
     * The technical name
     */
    private String technicalName;

    /**
     * The widget's image
     */
    private byte[] asset;
}
