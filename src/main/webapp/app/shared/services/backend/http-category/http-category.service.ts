import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AbstractHttpService } from '../abstract-http/abstract-http.service';
import { WidgetExecutionRequest } from '../../../models/widget-execution/widget-execution-request/widget-execution-request';
import { Observable } from 'rxjs';
import { WidgetParameter } from '../../../models/widget-parameter/widget-parameter';
import {WidgetDirectory} from "../../../models/widget/widget-directory";
import {CategoryDirectory} from "../../../models/category/category";

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
