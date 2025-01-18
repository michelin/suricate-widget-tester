import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';

import { HttpCategoryService } from './http-category.service';

describe('HttpCategoryService', () => {
  let service: HttpCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
    });
    service = TestBed.inject(HttpCategoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
