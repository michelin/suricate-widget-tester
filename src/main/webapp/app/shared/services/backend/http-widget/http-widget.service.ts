import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AbstractHttpService } from '../abstract-http/abstract-http.service';
import { WidgetExecutionRequest } from '../../../models/widget-execution/widget-execution-request/widget-execution-request';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class HttpWidgetService {
  /**
   * Widgets API endpoint
   */
  public static readonly widgetsApiEndpoint = `${AbstractHttpService.baseApiEndpoint}/v1`;

  /**
   * Constructor
   *
   * @param httpClient The HTTP client service
   */
  constructor(private readonly httpClient: HttpClient) {}

  /**
   * Run the widget according to the given parameters
   */
  public runWidget(widgetExecutionRequest: WidgetExecutionRequest): Observable<any> {
    const url = `${HttpWidgetService.widgetsApiEndpoint}/run`;

    return this.httpClient.post<any>(url, widgetExecutionRequest);
  }
}
