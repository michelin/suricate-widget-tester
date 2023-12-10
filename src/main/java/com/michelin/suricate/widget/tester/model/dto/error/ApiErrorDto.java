package com.michelin.suricate.widget.tester.model.dto.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.michelin.suricate.widget.tester.model.dto.AbstractDto;
import com.michelin.suricate.widget.tester.model.enums.ApiErrorEnum;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

/**
 * Api error DTO.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ApiErrorDto extends AbstractDto {
    private String message;
    private String key;
    private int status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    /**
     * Constructor.
     *
     * @param apiErrorEnum The API error enum
     */
    public ApiErrorDto(ApiErrorEnum apiErrorEnum) {
        this.message = apiErrorEnum.getMessage();
        this.key = apiErrorEnum.getKey();
        this.timestamp = new Date();
        this.status = apiErrorEnum.getStatus().value();
    }

    /**
     * Constructor.
     *
     * @param message  The error message
     * @param apiError The API error enum
     */
    public ApiErrorDto(String message, ApiErrorEnum apiError) {
        this(apiError);
        this.message = StringUtils.isBlank(message) ? apiError.getMessage() : message;
    }
}
