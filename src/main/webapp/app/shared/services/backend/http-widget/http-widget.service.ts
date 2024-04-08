import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AbstractHttpService } from '../abstract-http/abstract-http.service';
import { WidgetExecutionRequest } from '../../../models/widget-execution/widget-execution-request/widget-execution-request';
import { Observable } from 'rxjs';
import { WidgetParameter } from '../../../models/widget-parameter/widget-parameter';
import {WidgetDirectory} from "../../../models/widget/widget-directory";

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
   * Get the widget parameters
   *
   * @param category The category
   * @param widget The widget
   */
  public getWidgetParameters(category: string, widget: string): Observable<any> {
    const url = `${HttpWidgetService.widgetsApiEndpoint}/widgets/parameters?category=${category}&widget=${widget}`;

    return this.httpClient.get<WidgetParameter[]>(url);
  }

  /**
   * Run the widget according to the given parameters
   */
  public runWidget(widgetExecutionRequest: WidgetExecutionRequest): Observable<any> {
    const url = `${HttpWidgetService.widgetsApiEndpoint}/widgets/run`;

    return this.httpClient.post<any>(url, widgetExecutionRequest);
  }
}
