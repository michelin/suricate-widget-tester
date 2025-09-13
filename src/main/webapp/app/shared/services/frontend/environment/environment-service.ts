import { Injectable } from '@angular/core';

import { environment } from '../../../../../environments/environment';

@Injectable({
	providedIn: 'root'
})
export class EnvironmentService {
	/**
	 * Base url for http/ws calls
	 */
	public static readonly baseEndpoint = `${environment.baseUrl}`;
}
