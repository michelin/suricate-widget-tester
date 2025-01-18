import { ProjectWidget } from '../../project-widget/project-widget';

export interface WidgetExecutionResult {
  projectWidget?: ProjectWidget;
  widgetExecutionErrorMessage?: string;
}
