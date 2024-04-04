import { ProjectWidget } from '../../project-widget/project-widget';
import { WidgetExecutionRequest } from '../widget-execution-request/widget-execution-request';

export interface WidgetExecutionResult {
  projectWidget?: ProjectWidget;
  widgetExecutionErrorMessage?: string;
}
