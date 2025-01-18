import { TestBed } from '@angular/core/testing';

import { AbstractHttpService } from './abstract-http.service';

describe('AbstractHttpService', () => {
  let service: AbstractHttpService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AbstractHttpService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
