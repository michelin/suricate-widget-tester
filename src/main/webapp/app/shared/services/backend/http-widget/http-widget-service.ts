import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { ProjectWidget } from '../../../models/project-widget/project-widget';
import { WidgetExecutionRequest } from '../../../models/widget-execution/widget-execution-request/widget-execution-request';
import { WidgetParameter } from '../../../models/widget-parameter/widget-parameter';
import { AbstractHttpService } from '../abstract-http/abstract-http-service';

@Injectable({
	providedIn: 'root'
})
export class HttpWidgetService {
	private readonly httpClient = inject(HttpClient);

	/**
	 * Widgets API endpoint
	 */
	public static readonly widgetsApiEndpoint = `${AbstractHttpService.baseApiEndpoint}/v1`;

	/**
	 * Get the widget parameters
	 *
	 * @param category The category
	 * @param widget The widget
	 */
	public getWidgetParameters(category: string, widget: string): Observable<WidgetParameter[]> {
		const url = `${HttpWidgetService.widgetsApiEndpoint}/widgets/parameters?category=${category}&widget=${widget}`;

		return this.httpClient.get<WidgetParameter[]>(url);
	}

	/**
	 * Run the widget according to the given parameters
	 */
	public runWidget(widgetExecutionRequest: WidgetExecutionRequest): Observable<ProjectWidget> {
		const url = `${HttpWidgetService.widgetsApiEndpoint}/widgets/run`;

		return this.httpClient.post<ProjectWidget>(url, widgetExecutionRequest);
	}
}
