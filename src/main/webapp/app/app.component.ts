import {Component} from '@angular/core';
import {ProjectWidget} from './shared/models/project-widget/project-widget';
import {WidgetExecutionRequest} from "./shared/models/widget-execution/widget-execution-request/widget-execution-request";
import {WidgetExecutionResult} from "./shared/models/widget-execution/widget-execution-result/widget-execution-result";

@Component({
  selector: 'suricate-widget-tester-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  /**
   * The widget execution result
   */
  public widgetExecutionResult: WidgetExecutionResult | undefined;

  /**
   * Constructor
   */
  constructor() { }

  /**
   * Set the widget execution result from the child component
   *
   * @param widgetExecutionResult The widget execution result
   */
  public setWidgetExecutionResult(widgetExecutionResult: WidgetExecutionResult) {
    this.widgetExecutionResult = widgetExecutionResult;
  }
}
