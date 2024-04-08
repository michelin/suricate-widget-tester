import { WidgetParameterRequest } from '../../widget-parameter/widget-parameter-request';

export interface WidgetExecutionRequest {
  /**
   * Category directory name
   */
  category: string;

  /**
   * Widget directory name
   */
  widget: string;

  /**
   * Data of the previous execution of the widget
   */
  previousData?: string;

  /**
   * Widget parameters
   */
  parameters?: WidgetParameterRequest[];
}
