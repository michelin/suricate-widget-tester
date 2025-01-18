import { Injectable } from '@angular/core';

import { ProjectWidget } from '../../../shared/models/project-widget/project-widget';

@Injectable({
  providedIn: 'root'
})
export class MockedModelBuilderService {
  /**
   * Build a mocked project widget for the unit tests
   */
  public buildMockedProjectWidget(): ProjectWidget {
    return {
      id: 1,
      instantiateHtml: '',
      technicalName: '',
      cssContent: ''
    };
  }
}
