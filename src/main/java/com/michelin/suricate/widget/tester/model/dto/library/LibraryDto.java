package com.michelin.suricate.widget.tester.model.dto.library;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Library DTO.
 */
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LibraryDto extends AbstractDto {
    private Long id;
    private String technicalName;
    private byte[] asset;
}
