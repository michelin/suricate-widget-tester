package com.michelin.suricate.widget.tester.model.dto.library;

import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Library DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class LibraryDto extends AbstractDto {
    private Long id;
    private String technicalName;
    private byte[] asset;
}
