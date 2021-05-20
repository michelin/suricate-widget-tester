import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { WidgetExecutionRequest } from '../../../shared/models/widget-execution/widget-execution-request/widget-execution-request';
import { ProjectWidget } from '../../../shared/models/project-widget/project-widget';
import { HttpWidgetService } from '../../../shared/services/backend/http-widget/http-widget.service';
import { WidgetExecutionResult } from '../../../shared/models/widget-execution/widget-execution-result/widget-execution-result';
import { WidgetParameter } from '../../../shared/models/widget-parameter/widget-parameter';

@Component({
  selector: 'suricate-widget-configuration',
  templateUrl: './widget-configuration.component.html',
  styleUrls: ['./widget-configuration.component.scss']
})
export class WidgetConfigurationComponent implements OnInit {
  /**
   * Emit the error message of a widget
   */
  @Output()
  public widgetExecutionResultEmitEvent = new EventEmitter<WidgetExecutionResult>();

  /**
   * Form group
   */
  public runWidgetForm!: FormGroup;

  /**
   * The error message that can be retrieved when pushing a widget path into the input field
   */
  public onWidgetPathInputErrorMessage: string | undefined;

  /**
   * Constructor
   */
  constructor(private formBuilder: FormBuilder, private httpWidgetService: HttpWidgetService) {}

  /**
   * On init
   */
  ngOnInit(): void {
    this.runWidgetForm = this.formBuilder.group({
      path: ['', Validators.required],
      previousData: [''],
      parameters: this.formBuilder.array([])
    });
  }

  /**
   * Build a widget parameter form field
   *
   * @param parameterName An optional default parameter name
   */
  public buildWidgetParameterFormField(parameterName?: string): FormGroup {
    return this.formBuilder.group({
      parameterName: [parameterName ? parameterName : ''],
      parameterValue: ['']
    });
  }

  /**
   * On widget path input, load the parameters of the widget
   * and add them to the widget parameters form
   *
   * @param widgetPath The widget path
   */
  public onWidgetPathInput(widgetPath: string): void {
    if (this.isNotBlank(widgetPath)) {
      this.httpWidgetService.getWidgetParameters(widgetPath).subscribe(
        (widgetParameters: WidgetParameter[]) => {
          if (widgetParameters && widgetParameters.length > 0) {
            this.parameters.clear();

            widgetParameters.forEach((widgetParameter: WidgetParameter) => {
              this.parameters.push(this.buildWidgetParameterFormField(widgetParameter.name));
            });
          }
        },
        error => {
          this.onWidgetPathInputErrorMessage = error.error.message;
          this.parameters.clear();
        }
      );
    } else {
      this.parameters.clear();
    }
  }

  /**
   * Run the widget by validating the form
   */
  public runWidget(): void {
    if (!this.runWidgetForm.invalid) {
      const widgetExecutionRequest: WidgetExecutionRequest = {
        path: this.runWidgetForm.value.path
      };

      if (this.isNotBlank(this.runWidgetForm.value.previousData)) {
        widgetExecutionRequest.previousData = this.runWidgetForm.value.previousData;
      }

      if (this.runWidgetForm.value.parameters.length > 0) {
        this.runWidgetForm.value.parameters.forEach((parameter: { parameterName: string; parameterValue: string }) => {
          if (this.isNotBlank(parameter.parameterName) && this.isNotBlank(parameter.parameterValue)) {
            if (widgetExecutionRequest.parameters == null) {
              widgetExecutionRequest.parameters = [];
            }

            widgetExecutionRequest.parameters.push({
              name: parameter.parameterName,
              value: parameter.parameterValue
            });
          }
        });
      }

      this.httpWidgetService.runWidget(widgetExecutionRequest).subscribe(
        (projectWidget: ProjectWidget) => {
          const widgetExecutionResult: WidgetExecutionResult = {
            projectWidget: projectWidget,
            widgetExecutionRequest: widgetExecutionRequest
          };

          this.widgetExecutionResultEmitEvent.emit(widgetExecutionResult);
        },
        error => {
          const widgetExecutionResult: WidgetExecutionResult = {
            widgetExecutionErrorMessage: error.error.message,
            widgetExecutionRequest: widgetExecutionRequest
          };

          this.widgetExecutionResultEmitEvent.emit(widgetExecutionResult);
        }
      );
    }
  }

  /**
   * Return the list of widget parameters fields
   */
  get parameters(): FormArray {
    return this.runWidgetForm.get('parameters') as FormArray;
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
    return value != null && value !== '';
  }
}
