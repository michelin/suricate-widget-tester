import { NgFor, NgIf, NgOptimizedImage } from '@angular/common';
import { Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import {
  FormsModule,
  NgForm,
  ReactiveFormsModule,
  UntypedFormBuilder,
  UntypedFormGroup,
  Validators
} from '@angular/forms';
import { MatButton } from '@angular/material/button';
import { MatCheckbox } from '@angular/material/checkbox';
import { MatOptgroup, MatOption } from '@angular/material/core';
import { MatError, MatFormField, MatLabel } from '@angular/material/form-field';
import { MatIcon } from '@angular/material/icon';
import { MatInput } from '@angular/material/input';
import { MatSelect, MatSelectChange } from '@angular/material/select';

import { DataTypeEnum } from '../../../shared/enums/data-type.enum';
import { CategoryDirectory } from '../../../shared/models/category/category';
import { Configuration } from '../../../shared/models/config/configuration';
import { ProjectWidget } from '../../../shared/models/project-widget/project-widget';
import { WidgetExecutionRequest } from '../../../shared/models/widget-execution/widget-execution-request/widget-execution-request';
import { WidgetExecutionResult } from '../../../shared/models/widget-execution/widget-execution-result/widget-execution-result';
import { WidgetParameter } from '../../../shared/models/widget-parameter/widget-parameter';
import { HttpCategoryService } from '../../../shared/services/backend/http-category/http-category.service';
import { HttpConfigurationService } from '../../../shared/services/backend/http-configuration/http-configuration.service';
import { HttpWidgetService } from '../../../shared/services/backend/http-widget/http-widget.service';
import { FormField } from '../../../shared/services/frontend/form/form-field';
import { FileUtils } from '../../services/utils/file.utils';

@Component({
  selector: 'suricate-widget-configuration',
  templateUrl: './widget-configuration.component.html',
  styleUrls: ['./widget-configuration.component.scss'],
  standalone: true,
  imports: [
    NgOptimizedImage,
    FormsModule,
    ReactiveFormsModule,
    MatFormField,
    MatLabel,
    MatSelect,
    NgFor,
    MatOptgroup,
    MatOption,
    NgIf,
    MatError,
    MatInput,
    MatButton,
    MatCheckbox,
    MatIcon
  ]
})
export class WidgetConfigurationComponent implements OnInit {
  @ViewChild('widgetForm')
  widgetForm: NgForm;

  /**
   * Emit the error message of a widget
   */
  @Output()
  public widgetExecutionResultEmitEvent = new EventEmitter<WidgetExecutionResult>();

  /**
   * The files that have been uploaded
   * The key is the form field name
   */
  public files: Record<string, { fileName: string; base64Url: string }> = {};

  /**
   * If it's not an image we set the filename
   */
  public filename: string;

  /**
   * Form group
   */
  public runWidgetForm: UntypedFormGroup;

  /**
   * The error message that can be retrieved when pushing a widget path into the input field
   */
  public onWidgetPathInputErrorMessage: string;

  /**
   * The category directories
   */
  public categoryDirectories: CategoryDirectory[];

  /**
   * The repository name
   */
  public repository: string;

  /**
   * The widget parameters form fields
   */
  public widgetParamsFormField: FormField[] = [];

  /**
   * The data type enum
   */
  public dataType = DataTypeEnum;

  /**
   * The selected category
   */
  public selectedCategory: string;

  /**
   * The selected widget
   */
  public selectedWidget: string;

  /**
   * Constructor
   *
   * @param formBuilder The form builder service
   * @param httpWidgetService The http widget service
   * @param httpConfigurationService The http configuration service
   * @param httpCategoryService The http category service
   */
  constructor(
    private readonly formBuilder: UntypedFormBuilder,
    private readonly httpWidgetService: HttpWidgetService,
    private readonly httpConfigurationService: HttpConfigurationService,
    private readonly httpCategoryService: HttpCategoryService
  ) {}

  /**
   * On init
   */
  ngOnInit(): void {
    this.httpConfigurationService.getRepository().subscribe((config: Configuration) => {
      this.repository = config.repository;
    });

    this.httpCategoryService.getCategoryDirectories().subscribe((categoryDirectories: CategoryDirectory[]) => {
      this.categoryDirectories = categoryDirectories;
    });

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
  public getWidgetParameters(event?: MatSelectChange): void {
    if (event) {
      this.selectedCategory = event.value.split('/')[0];
      this.selectedWidget = event.value.split('/')[1];
    }

    this.httpWidgetService.getWidgetParameters(this.selectedCategory, this.selectedWidget).subscribe(
      (widgetParameters: WidgetParameter[]) => {
        if (widgetParameters && widgetParameters.length > 0) {
          const oldPath = this.runWidgetForm.controls['path'].value;
          const oldPreviousData = this.runWidgetForm.controls['previousData'].value;

          const oldControls: Record<string, { value: string }> = {};
          this.widgetParamsFormField.forEach((field: FormField) => {
            if (this.runWidgetForm.controls[field.name + '-value'].value) {
              oldControls[field.name + '-value'] = { value: this.runWidgetForm.controls[field.name + '-value'].value };
            }
          });

          this.resetScreen();

          this.runWidgetForm.get('path').setValue(oldPath);
          this.runWidgetForm.get('previousData').setValue(oldPreviousData);

          widgetParameters.forEach((widgetParameter: WidgetParameter) => {
            this.widgetParamsFormField.push({
              name: widgetParameter.name,
              type: widgetParameter.type,
              required: widgetParameter.required,
              possibleValues: widgetParameter.possibleValuesMap
            });

            this.runWidgetForm.registerControl(
              widgetParameter.name + '-name',
              this.formBuilder.control(widgetParameter.name)
            );

            this.runWidgetForm.registerControl(
              widgetParameter.name + '-value',
              this.formBuilder.control(
                oldControls[widgetParameter.name + '-value']
                  ? oldControls[widgetParameter.name + '-value'].value
                  : widgetParameter.defaultValue
              )
            );
          });
        }
      },
      (error) => {
        this.resetScreen();
        this.onWidgetPathInputErrorMessage = error.error.message;
      }
    );
  }

  /**
   * Run the widget by validating the form
   */
  public runWidget(): void {
    this.runWidgetForm.markAllAsTouched();

    if (!this.runWidgetForm.invalid) {
      const widgetExecutionRequest: WidgetExecutionRequest = {
        category: this.selectedCategory,
        widget: this.selectedWidget
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
            value: String(this.runWidgetForm.controls[field.name + '-value'].value)
          });
        }
      });

      this.httpWidgetService.runWidget(widgetExecutionRequest).subscribe(
        (projectWidget: ProjectWidget) => {
          const widgetExecutionResult: WidgetExecutionResult = {
            projectWidget: projectWidget
          };

          this.widgetExecutionResultEmitEvent.emit(widgetExecutionResult);
        },
        (error) => {
          const widgetExecutionResult: WidgetExecutionResult = {
            widgetExecutionErrorMessage: error.error.message
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
    this.widgetParamsFormField.forEach((field: FormField) => {
      this.runWidgetForm.removeControl(field.name + '-name');
      this.runWidgetForm.removeControl(field.name + '-value');
    });

    this.widgetParamsFormField = [];
    this.onWidgetPathInputErrorMessage = undefined;
    this.widgetExecutionResultEmitEvent.emit(undefined);

    // Clear form fields and validations
    this.widgetForm.resetForm();
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

      this.files[formField.name] = {
        fileName: fileName,
        base64Url: base64String
      };

      this.runWidgetForm.controls[formField.name + '-value'].setValue(base64String);
      this.runWidgetForm.markAsDirty();
      this.runWidgetForm.markAsTouched();
    });
  }

  /**
   * Check if the base64 url is an image
   *
   * @param formField The form field to check
   */
  public isBase64UrlIsAnImage(formField: FormField): boolean {
    return FileUtils.isBase64UrlIsAnImage(this.files[formField.name].base64Url);
  }

  /**
   * Get the base64 url of the file
   *
   * @param formField The form field to get the base64 url
   */
  public getBase64Url(formField: FormField) {
    return this.files[formField.name].base64Url;
  }

  /**
   * Get the file name
   *
   * @param formField The form field to get the file name
   */
  public getFileName(formField: FormField) {
    return this.files[formField.name].fileName;
  }

  /**
   * Check if the file is existing
   *
   * @param formField The form field to check
   */
  public isFileExisting(formField: FormField): boolean {
    return this.files[formField.name] != null;
  }

  /**
   * Check if the given form field has the given error
   *
   * @param formFieldName The form field to check
   * @param errorName The error to check
   */
  public checkError(formFieldName: string, errorName: string): boolean | undefined {
    return (
      this.runWidgetForm.get(formFieldName)?.hasError(errorName) &&
      (this.runWidgetForm.get(formFieldName)?.touched || this.runWidgetForm.get(formFieldName)?.dirty)
    );
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
