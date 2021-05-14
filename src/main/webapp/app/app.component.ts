import { Component } from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpWidgetService} from "./services/backend/http-widget/http-widget.service";
import {WidgetExecutionRequest} from "./models/widget-execution-request/widget-execution-request";
import {ProjectWidget} from "./models/project-widget/project-widget";

@Component({
  selector: 'suricate-widget-tester-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  /**
   * The project widget to display according to the given configuration
   */
  public projectWidget: ProjectWidget | undefined;

  /**
   * The error message that can occur during the
   */
  public widgetExecutionErrorMessage: string | undefined;

  /**
   * Constructor
   */
  constructor() { }

  /**
   * Set the configured project widget from the child component
   *
   * @param projectWidget The configured project widget
   */
  setConfiguredProjectWidget(projectWidget: ProjectWidget) {
    this.projectWidget = projectWidget;
  }

  /**
   * Set the widget execution error message from the child component
   *
   * @param errorMessage The widget execution error message
   */
  setWidgetExecutionErrorMessage(errorMessage: string) {
    this.widgetExecutionErrorMessage = errorMessage;
  }
}
