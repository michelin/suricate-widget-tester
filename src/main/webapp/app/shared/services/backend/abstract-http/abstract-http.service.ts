import { Injectable } from '@angular/core';

import { EnvironmentService } from '../../frontend/environment/environment.service';

@Injectable({
	providedIn: 'root'
})
export class AbstractHttpService {
	/**
	 * The base API url
	 */
	public static readonly baseApiEndpoint = `${EnvironmentService.baseEndpoint}/api`;
}
