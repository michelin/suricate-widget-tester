import { TestBed } from '@angular/core/testing';

import { HttpCategoryService } from './http-category.service';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

describe('HttpCategoryService', () => {
  let service: HttpCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
    imports: [],
    providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
});

    service = TestBed.inject(HttpCategoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
