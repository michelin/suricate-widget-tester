import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { CategoryDirectory } from '../../../models/category/category';
import { AbstractHttpService } from '../abstract-http/abstract-http.service';

@Injectable({
  providedIn: 'root'
})
export class HttpCategoryService {
  /**
   * Widgets API endpoint
   */
  public static readonly categoriesApiEndpoint = `${AbstractHttpService.baseApiEndpoint}/v1`;

  /**
   * Constructor
   *
   * @param httpClient The HTTP client service
   */
  constructor(private readonly httpClient: HttpClient) {}

  /**
   * Get the widget name list
   */
  public getCategoryDirectories(): Observable<CategoryDirectory[]> {
    const url = `${HttpCategoryService.categoriesApiEndpoint}/categories/directories`;

    return this.httpClient.get<CategoryDirectory[]>(url);
  }
}
