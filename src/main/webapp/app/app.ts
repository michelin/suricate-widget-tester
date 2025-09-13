import { Component } from '@angular/core';
import { MatDivider } from '@angular/material/divider';

import { DashboardScreen } from './dashboard/components/dashboard-screen/dashboard-screen';
import { WidgetConfiguration } from './dashboard/components/widget-configuration/widget-configuration';
import { WidgetExecutionResult } from './shared/models/widget-execution/widget-execution-result/widget-execution-result';

@Component({
	selector: 'suricate-widget-tester-root',
	templateUrl: './app.html',
	styleUrls: ['./app.scss'],
	imports: [WidgetConfiguration, DashboardScreen, MatDivider]
})
export class App {
	/**
	 * The widget execution result
	 */
	public widgetExecutionResult: WidgetExecutionResult;

	/**
	 * Set the widget execution result from the child component
	 *
	 * @param widgetExecutionResult The widget execution result
	 */
	public setWidgetExecutionResult(widgetExecutionResult: WidgetExecutionResult) {
		this.widgetExecutionResult = widgetExecutionResult;
	}
}
