import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { HttpConfigurationService } from './http-configuration.service';

describe('HttpConfigurationService', () => {
  let service: HttpConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
    });
    service = TestBed.inject(HttpConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
