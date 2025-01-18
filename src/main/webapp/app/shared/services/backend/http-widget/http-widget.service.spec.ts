import { TestBed } from '@angular/core/testing';

import { HttpWidgetService } from './http-widget.service';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('HttpWidgetService', () => {
  let service: HttpWidgetService;

  beforeEach(() => {
    TestBed.configureTestingModule({
    imports: [],
    providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
});

    service = TestBed.inject(HttpWidgetService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
