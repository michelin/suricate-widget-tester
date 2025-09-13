import { NgClass } from '@angular/common';
import { Component, inject, Input, OnInit } from '@angular/core';

import { Spinner } from '../../../../shared/components/spinner/spinner';
import { WidgetHtmlDirective } from '../../../../shared/directives/widget-html-directive';
import { ProjectWidget } from '../../../../shared/models/project-widget/project-widget';
import { SafeHtmlPipe } from '../../../../shared/pipes/safe-html/safe-html-pipe';
import { LibraryService } from '../../../../shared/services/frontend/library/library-service';

@Component({
	selector: 'suricate-dashboard-screen-widget',
	templateUrl: './dashboard-screen-widget.html',
	styleUrls: ['./dashboard-screen-widget.scss'],
	imports: [NgClass, Spinner, WidgetHtmlDirective, SafeHtmlPipe]
})
export class DashboardScreenWidget implements OnInit {
	private readonly libraryService = inject(LibraryService);

	/**
	 * The projectWidget to display
	 */
	@Input()
	public projectWidget: ProjectWidget;

	/**
	 * Is the widget loading or not
	 */
	public loading = true;

	/**
	 * Init method
	 */
	ngOnInit(): void {
		this.libraryService.allExternalLibrariesLoaded.subscribe((areExternalLibrariesLoaded: boolean) => {
			this.loading = !areExternalLibrariesLoaded;
		});
	}
}
