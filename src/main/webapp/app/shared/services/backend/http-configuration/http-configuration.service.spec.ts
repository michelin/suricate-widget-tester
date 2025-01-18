import { TestBed } from '@angular/core/testing';

import { HttpConfigurationService } from './http-configuration.service';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('HttpConfigurationService', () => {
  let service: HttpConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
    imports: [],
    providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
});

    service = TestBed.inject(HttpConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
