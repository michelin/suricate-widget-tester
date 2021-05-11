import { TestBed } from '@angular/core/testing';

import { HttpWidgetService } from './http-widget.service';

describe('HttpWidgetService', () => {
  let service: HttpWidgetService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(HttpWidgetService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
