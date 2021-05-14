import { TestBed } from '@angular/core/testing';

import { HttpWidgetService } from './http-widget.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('HttpWidgetService', () => {
  let service: HttpWidgetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });

    service = TestBed.inject(HttpWidgetService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
