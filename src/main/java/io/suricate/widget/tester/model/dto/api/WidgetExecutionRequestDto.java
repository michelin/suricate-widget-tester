package io.suricate.widget.tester.model.dto.api;

import io.suricate.widget.tester.model.dto.AbstractDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Widget execution request DTO, received from Front-End
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class WidgetExecutionRequestDto extends AbstractDto {

  /**
   * Path of the widget
   */
  private String path;

  /**
   * Data of the previous execution of the widget
   */
  private String previousData;

  /**
   * Proxy host
   */
  private String proxyHost;

  /**
   * Proxy port
   */
  private Integer proxyPort;

  /**
   * No proxy domains
   */
  private String noProxyDomains;

  /**
   * List of widget parameters
   */
  private List<WidgetParametersRequestDto> parameters = new ArrayList<>();
}
