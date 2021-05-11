/**
 * The widget execution request
 */
import {WidgetParameter} from "../widget-parameter/widget-parameter";

export interface WidgetExecutionRequest {
  /**
   * Path of the widget
   */
  path: string;

  /**
   * Data of the previous execution of the widget
   */
  previousData?: string;

  /**
   * Proxy host
   */
  proxyHost?: number;

  /**
   * Proxy port
   */
  proxyPort?: number;

  /**
   * No proxy domains
   */
  noProxyDomains?: string;
  parameters?: WidgetParameter[];
}
