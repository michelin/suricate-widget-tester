import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { Configuration } from '../../../models/config/configuration';
import { AbstractHttpService } from '../abstract-http/abstract-http.service';

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
