import { TestBed } from '@angular/core/testing';

import { HttpConfigurationService } from './http-configuration.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('HttpConfigurationService', () => {
  let service: HttpConfigurationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(HttpConfigurationService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
