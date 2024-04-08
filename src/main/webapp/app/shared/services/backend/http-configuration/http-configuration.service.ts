import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AbstractHttpService } from '../abstract-http/abstract-http.service';
import { WidgetExecutionRequest } from '../../../models/widget-execution/widget-execution-request/widget-execution-request';
import { Observable } from 'rxjs';
import { WidgetParameter } from '../../../models/widget-parameter/widget-parameter';
import {WidgetDirectory} from "../../../models/widget/widget-directory";
import {Configuration} from "../../../models/config/configuration";

@Injectable({
  providedIn: 'root'
})
export class HttpConfigurationService {
  /**
   * Widgets API endpoint
   */
  public static readonly configurationsApiEndpoint = `${AbstractHttpService.baseApiEndpoint}/v1`;

  /**
   * Constructor
   *
   * @param httpClient The HTTP client service
   */
  constructor(private readonly httpClient: HttpClient) {}

  /**
   * Get the configured widget repository
   */
  public getRepository(): Observable<Configuration> {
    const url = `${HttpConfigurationService.configurationsApiEndpoint}/configurations/repository`;

    return this.httpClient.get<Configuration>(url);
  }
}
