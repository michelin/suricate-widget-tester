import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';

import { CategoryDirectory } from '../../../models/category/category';
import { AbstractHttpService } from '../abstract-http/abstract-http-service';

@Injectable({
	providedIn: 'root'
})
export class HttpCategoryService {
	private readonly httpClient = inject(HttpClient);

	/**
	 * Widgets API endpoint
	 */
	public static readonly categoriesApiEndpoint = `${AbstractHttpService.baseApiEndpoint}/v1`;

	/**
	 * Get the widget name list
	 */
	public getCategoryDirectories(): Observable<CategoryDirectory[]> {
		const url = `${HttpCategoryService.categoriesApiEndpoint}/categories/directories`;

		return this.httpClient.get<CategoryDirectory[]>(url);
	}
}
