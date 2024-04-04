import { TestBed } from '@angular/core/testing';

import { HttpCategoryService } from './http-category.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('HttpCategoryService', () => {
  let service: HttpCategoryService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });

    service = TestBed.inject(HttpCategoryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
