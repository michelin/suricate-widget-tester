import {Component, OnInit, Output} from '@angular/core';
import {FormArray, FormBuilder, FormGroup, Validators} from "@angular/forms";
import {WidgetExecutionRequest} from "../../models/widget-execution-request/widget-execution-request";
import {ProjectWidget} from "../../models/project-widget/project-widget";
import {HttpWidgetService} from "../../services/backend/http-widget/http-widget.service";
import { EventEmitter } from "@angular/core";

@Component({
  selector: 'widget-configuration',
  templateUrl: './widget-configuration.component.html',
  styleUrls: ['./widget-configuration.component.scss']
})
export class WidgetConfigurationComponent implements OnInit {

  /**
   * Emit the retrieved widget execution instance
   */
  @Output()
  public projectWidgetEmitEvent = new EventEmitter<ProjectWidget>();

  /**
   * Emit the error message of a widget
   */
  @Output()
  public widgetExecutionErrorEmitEvent = new EventEmitter<string>();

  /**
   * Form group
   */
  public runWidgetForm!: FormGroup;

  /**
   * Constructor
   */
  constructor(private formBuilder: FormBuilder,
              private httpWidgetService: HttpWidgetService) { }

  /**
   * On init
   */
  ngOnInit(): void {
    this.runWidgetForm = this.formBuilder.group({
      path: ['', Validators.required],
      previousData: [''],
      parameters: this.formBuilder.array([this.buildWidgetParameterFormField()])
    });
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
   * Run the widget by validating the form
   */
  public runWidget() {
    if (!this.runWidgetForm.invalid) {
      const widgetExecutionRequest: WidgetExecutionRequest = {
        path: this.runWidgetForm.value.path,
      };

      if (this.isNotBlank(this.runWidgetForm.value.previousData)) {
        widgetExecutionRequest.previousData = this.runWidgetForm.value.previousData;
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

      this.httpWidgetService.runWidget(widgetExecutionRequest)
        .subscribe(
          (projectWidget: ProjectWidget) => {
            this.projectWidgetEmitEvent.emit(projectWidget);
            this.widgetExecutionErrorEmitEvent.emit(undefined);
          },
          error => {
            this.projectWidgetEmitEvent.emit(undefined);
            this.widgetExecutionErrorEmitEvent.emit(error.error.message)
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
