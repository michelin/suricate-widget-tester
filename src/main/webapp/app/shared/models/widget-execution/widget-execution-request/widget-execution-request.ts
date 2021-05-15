/**
 * The widget execution request
 */
import { WidgetParameter } from '../../widget-parameter/widget-parameter';

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
   * Widget parameters
   */
  parameters?: WidgetParameter[];
}
