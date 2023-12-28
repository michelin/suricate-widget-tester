import { Injectable } from '@angular/core';
import { UntypedFormBuilder } from '@angular/forms';
import { NgGridItemConfig } from 'angular2-grid';
import { ProjectWidget } from '../../../shared/models/project-widget/project-widget';

@Injectable({
  providedIn: 'root'
})
export class MockedModelBuilderService {
  /**
   * Constructor
   *
   * @param formBuilder The form builder
   */
  constructor(private readonly formBuilder: UntypedFormBuilder) {}

  /**
   * Build a mocked project widget for the unit tests
   */
  public buildMockedProjectWidget(): ProjectWidget {
    return {
      instantiateHtml: '',
      technicalName: '',
      cssContent: ''
    };
  }

  /**
   * Build a mocked gridItemConfig for the unit tests
   */
  public buildGridStackItem(): NgGridItemConfig {
    return {
      col: 0,
      row: 0,
      sizey: 50,
      sizex: 50
    };
  }
}
