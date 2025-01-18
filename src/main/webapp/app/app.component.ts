import { Component } from '@angular/core';
import { MatDivider } from '@angular/material/divider';

import { DashboardScreenComponent } from './dashboard/components/dashboard-screen/dashboard-screen.component';
import { WidgetConfigurationComponent } from './dashboard/components/widget-configuration/widget-configuration.component';
import { WidgetExecutionResult } from './shared/models/widget-execution/widget-execution-result/widget-execution-result';

@Component({
  selector: 'suricate-widget-tester-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  imports: [WidgetConfigurationComponent, DashboardScreenComponent, MatDivider]
})
export class AppComponent {
  /**
   * The widget execution result
   */
  public widgetExecutionResult: WidgetExecutionResult;

  /**
   * Set the widget execution result from the child component
   *
   * @param widgetExecutionResult The widget execution result
   */
  public setWidgetExecutionResult(widgetExecutionResult: WidgetExecutionResult) {
    this.widgetExecutionResult = widgetExecutionResult;
  }
}
