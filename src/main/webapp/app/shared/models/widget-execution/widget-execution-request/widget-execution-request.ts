/**
 * The widget execution request
 */
import {WidgetParameterRequest} from "../../widget-parameter/widget-parameter-request";

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
  parameters?: WidgetParameterRequest[];
}
