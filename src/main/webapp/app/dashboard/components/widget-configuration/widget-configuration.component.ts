import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import {AbstractControl, Form, FormArray, FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import { WidgetExecutionRequest } from '../../../shared/models/widget-execution/widget-execution-request/widget-execution-request';
import { ProjectWidget } from '../../../shared/models/project-widget/project-widget';
import { HttpWidgetService } from '../../../shared/services/backend/http-widget/http-widget.service';
import { WidgetExecutionResult } from '../../../shared/models/widget-execution/widget-execution-result/widget-execution-result';
import { WidgetParameter } from '../../../shared/models/widget-parameter/widget-parameter';
import {DataTypeEnum} from "../../../shared/enums/data-type.enum";
import {FormField} from "../../../shared/services/frontend/form/form-field";
import {Observable} from "rxjs";
import {FileUtils} from "../../services/utils/file.utils";

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
  public widgetExecutionResultEmitEvent = new EventEmitter<WidgetExecutionResult | undefined>();

  /**
   * The image as base 64
   */
  public imgBase64: string | ArrayBuffer | undefined;

  /**
   * If it's not an image we set the filename
   */
  public filename: string | undefined;

  /**
   * Form group
   */
  public runWidgetForm!: FormGroup;

  /**
   * The error message that can be retrieved when pushing a widget path into the input field
   */
  public onWidgetPathInputErrorMessage: string | undefined;

  /**
   * Entered widget path
   */
  public widgetPath: string | undefined;

  /**
   * The widget parameters form fields
   */
  public widgetParamsFormField: FormField[] = [];

  /**
   * The data type enum
   */
  public dataType = DataTypeEnum;

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
      previousData: ['']
    });
  }

  /**
   * Load the parameters of the widgets.
   *
   * Triggered when setting a widget path in the dedicated input
   * and also triggered when a reload is asked manually with the dedicated
   * button
   *
   * @param event The input event
   */
  public getWidgetParameters(event?: Event): void {
    if (event) {
      this.widgetPath = (<HTMLInputElement>event.target).value;
    }

    if (this.widgetPath) {
      this.httpWidgetService.getWidgetParameters(this.widgetPath).subscribe(
        (widgetParameters: WidgetParameter[]) => {
          if (widgetParameters && widgetParameters.length > 0) {
            this.resetScreen();

            widgetParameters.forEach((widgetParameter: WidgetParameter) => {
              this.widgetParamsFormField.push({
                name: widgetParameter.name,
                type: widgetParameter.type,
                required: widgetParameter.required,
                possibleValues: widgetParameter.possibleValuesMap
              });

              this.runWidgetForm.registerControl(widgetParameter.name + '-name',
                  this.formBuilder.control(widgetParameter.name))

              this.runWidgetForm.registerControl(widgetParameter.name + '-value',
                  this.formBuilder.control(this.runWidgetForm.controls[widgetParameter.name + '-value'] ?
                      this.runWidgetForm.controls[widgetParameter.name + '-value'].value : widgetParameter.defaultValue))
            });
          }
        },
        error => {
          this.resetScreen();
          this.onWidgetPathInputErrorMessage = error.error.message;
        }
      );
    } else {
      this.resetScreen();
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

      this.widgetParamsFormField.forEach((field: FormField) => {
        if (this.isNotBlank(this.runWidgetForm.controls[field.name + '-value'].value)) {
          if (widgetExecutionRequest.parameters == null) {
            widgetExecutionRequest.parameters = [];
          }

          widgetExecutionRequest.parameters.push({
            name: field.name,
            value: this.runWidgetForm.controls[field.name + '-value'].value
          });
        }
      });

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
   * Reset the widget parameters and the displayed widget
   */
  public resetScreen(): void {
    this.widgetParamsFormField = [];
    this.onWidgetPathInputErrorMessage = undefined;
    this.widgetExecutionResultEmitEvent.emit(undefined);
  }

  /**
   * Manage the change event return by the input
   *
   * @param event The file change event
   * @param formField The form field that triggered the event
   */
  public onFileChange(event: Event, formField: FormField): void {
    this.convertFileBase64(event, formField);
  }

  /**
   * Convert a file into base64
   *
   * @param event The change event
   * @param formField The form field that triggered the event
   */
  public convertFileBase64(event: Event, formField: FormField): void {
    const inputElement = event.target as HTMLInputElement;
    if (!inputElement.files || inputElement.files.length === 0) {
      return;
    }

    const file: File = inputElement.files[0];
    FileUtils.convertFileToBase64(file).subscribe((base64Url: string | ArrayBuffer) => {
      const base64String = base64Url as string;
      const fileName = file.name;

      this.setBase64File(base64String, fileName);
      this.runWidgetForm.controls[formField.name + '-value'].setValue(base64String);
      this.runWidgetForm.markAsDirty();
      this.runWidgetForm.markAsTouched();
    });
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

  /**
   * Use to display image or filename
   *
   * @param base64Url The base64 url of the image
   * @param filename The filename if the file is not an image
   */
  private setBase64File(base64Url: string, filename: string): void {
    if (FileUtils.isBase64UrlIsAnImage(base64Url)) {
      this.imgBase64 = base64Url;
      this.filename = undefined;
    } else {
      this.imgBase64 = undefined;
      this.filename = filename;
    }
  }
}
