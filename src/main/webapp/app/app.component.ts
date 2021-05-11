import { Component } from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {HttpWidgetService} from "./services/backend/http-widget/http-widget.service";
import {WidgetExecutionRequest} from "./models/widget-execution-request/widget-execution-request";

@Component({
  selector: 'suricate-widget-tester-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {

  /**
   * Form group
   */
  public runWidgetForm!: FormGroup;

  /**
   * Error response of the widget execution
   */
  public responseError?: string;

  /**
   * Constructor
   *
   * @param formBuilder The form builder
   * @param httpWidgetService The HTTP widget service
   */
  constructor(private formBuilder: FormBuilder,
              private httpWidgetService: HttpWidgetService) {
    this.runWidgetForm = this.formBuilder.group({
      path: ['', Validators.required],
      previousData: [''],
      parameters: this.formBuilder.array([this.buildWidgetParameterFormField()]),
      proxyHost: [''],
      proxyPort: [''],
      noProxyDomains: [''],
    });
  }

  /**
   * Run the widget by validating the form
   */
  public runWidget() {
    const widgetExecutionRequest: WidgetExecutionRequest = {
      path: this.runWidgetForm.value.path,
    };

    if (this.isNotBlank(this.runWidgetForm.value.previousData)) {
      widgetExecutionRequest.previousData = this.runWidgetForm.value.previousData;
    }

    if (this.isNotBlank(this.runWidgetForm.value.proxyHost)) {
      widgetExecutionRequest.proxyHost = this.runWidgetForm.value.proxyHost;
    }

    if (this.isNotBlank(this.runWidgetForm.value.proxyPort)) {
      widgetExecutionRequest.proxyPort = this.runWidgetForm.value.proxyPort;
    }

    if (this.isNotBlank(this.runWidgetForm.value.noProxyDomains)) {
      widgetExecutionRequest.noProxyDomains = this.runWidgetForm.value.noProxyDomains;
    }

    if (this.runWidgetForm.value.parameters.length > 0) {
      this.runWidgetForm.value.parameters.forEach((parameter: { parameterName: string; parameterValue: string; }) => {
        if (this.isNotBlank(parameter.parameterName) && this.isNotBlank(parameter.parameterValue)) {
          if (widgetExecutionRequest.parameters == null) {
            widgetExecutionRequest.parameters = [];
          }

          widgetExecutionRequest.parameters.push({
            name: parameter.parameterName,
            value: parameter.parameterValue
          });
        }
      })
    }

    this.httpWidgetService.runWidget(widgetExecutionRequest).subscribe(value => {
      console.warn(value);
    },
    error => this.responseError = error.error.message
    );
  }

  /**
   * Return the list of widget parameters fields
   */
  get parameters(): FormArray {
    return this.runWidgetForm.get('parameters') as FormArray;
  }

  /**
   * Build a widget parameter form field
   */
  public buildWidgetParameterFormField(): FormGroup {
    return this.formBuilder.group({
      parameterName: [''],
      parameterValue: ['']
    });
  }

  /**
   * Add a widget parameter form field
   */
  public addParameter() {
    this.parameters.push(this.buildWidgetParameterFormField());
  }

  /**
   * Remove a widget parameter form field
   *
   * @param index The index of the form field to remove
   */
  public removeParameter(index: number) {
    this.parameters.removeAt(index)
  }

  /**
   * Check if the given form field has the given error
   *
   * @param formFieldName The form field to check
   * @param errorName The error to check
   */
  public checkError(formFieldName: string, errorName: string): boolean | undefined {
    return this.runWidgetForm.get(formFieldName)?.hasError(errorName);
  }

  /**
   * Check if the given string value is not blank
   *
   * @param value The value to check
   */
  public isNotBlank(value: string): boolean {
    return value != null && value != '';
  }
}
