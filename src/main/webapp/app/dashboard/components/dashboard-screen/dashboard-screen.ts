import { Component, effect, ElementRef, inject, input, Renderer2, ViewChild } from '@angular/core';
import { MatIcon } from '@angular/material/icon';
import {
	KtdGridComponent,
	KtdGridItemComponent,
	KtdGridItemPlaceholder,
	KtdGridLayout,
	KtdGridLayoutItem
} from '@katoid/angular-grid-layout';

import { GridOptions } from '../../../shared/models/backend/grid/grid-options';
import { WidgetExecutionResult } from '../../../shared/models/backend/widget-execution/widget-execution-result/widget-execution-result';
import { HttpLibraryService } from '../../../shared/services/backend/http-library/http-library-service';
import { GridItemUtils } from '../../../shared/utils/grid-item.utils';
import { LibraryService } from '../../services/library/library-service';
import { DashboardScreenWidget } from './dashboard-screen-widget/dashboard-screen-widget';

declare global {
	interface Window {
		page_loaded: boolean;
	}
}

@Component({
	selector: 'suricate-dashboard-screen',
	templateUrl: './dashboard-screen.html',
	styleUrls: ['./dashboard-screen.scss'],
	imports: [MatIcon, KtdGridComponent, KtdGridItemComponent, DashboardScreenWidget, KtdGridItemPlaceholder]
})
export class DashboardScreen {
	private readonly renderer = inject(Renderer2);
	private readonly libraryService = inject(LibraryService);

	/**
	 * Reference on the span containing all the required JS libraries
	 */
	@ViewChild('externalJsLibraries')
	public externalJsLibrariesSpan!: ElementRef<HTMLSpanElement>;

	/**
	 * The widget execution result
	 */
	public widgetExecutionResult = input<WidgetExecutionResult>();

	/**
	 * The path of the widget folder
	 */
	public widgetPath = input<string>();

	/**
	 * The grid options
	 */
	public gridOptions: GridOptions;

	/**
	 * All the grids of the dashboard
	 */
	public currentGrid: KtdGridLayout = [];

	/**
	 * Constructor.
	 */
	constructor() {
		// Inject this variable in the window scope because some widgets use it to init the js
		effect(() => {
			this.widgetExecutionResult();

			if (!window.page_loaded) {
				window.page_loaded = true;
			}

			this.initGridStackOptions();
			this.initGrid();
			this.addExternalJSLibrariesToTheDOM();
		});
	}

	/**
	 * Create the list of grid items used to display widgets on the grids
	 */
	private initGrid(): void {
		this.currentGrid = [];
		if (this.widgetExecutionResult()?.projectWidget) {
			this.currentGrid = this.getGridLayoutFromProjectWidgets();
		}
	}

	/**
	 * Init the options for Grid Stack plugin
	 */
	private initGridStackOptions(): void {
		this.gridOptions = {
			cols: 5,
			rowHeight: 360,
			gap: 5,
			draggable: true,
			resizable: true,
			compactType: undefined
		};
	}

	/**
	 * Get the list of GridItemConfigs from project widget
	 */
	private getGridLayoutFromProjectWidgets(): KtdGridLayout {
		const layout: KtdGridLayout = [];

		layout.push({
			id: String(this.widgetExecutionResult().projectWidget.id),
			x: 0,
			y: 0,
			w: 1,
			h: 1
		});

		return layout;
	}

	/**
	 * For each JS libraries linked with the project, create a script element with the URL of the library
	 * and a callback which notify subscribers when the library is loaded.
	 * Always clear the previously loaded libraries before adding new ones to handle widget execution result changes.
	 */
	public addExternalJSLibrariesToTheDOM(): void {
		if (this.widgetExecutionResult()?.projectWidget) {
			if (this.widgetExecutionResult().projectWidget.librariesNames) {
				this.libraryService.init(this.widgetExecutionResult().projectWidget.librariesNames.length);

				this.widgetExecutionResult().projectWidget.librariesNames.forEach((libraryName) => {
					const script: HTMLScriptElement = document.createElement('script');
					script.type = 'text/javascript';
					script.src = HttpLibraryService.getContentUrl(libraryName);
					script.onload = () => this.libraryService.markScriptAsLoaded(libraryName);
					script.async = false;

					this.renderer.appendChild(this.externalJsLibrariesSpan.nativeElement, script);
				});
			} else {
				this.libraryService.emitAreJSScriptsLoaded(true);
			}
		}
	}

	/**
	 * When the layout is updated
	 * @param layout The new layout
	 */
	public onLayoutUpdated(layout: KtdGridLayout) {
		if (this.isGridItemsHasMoved(layout)) {
			this.currentGrid = layout;
		}
	}

	/**
	 * Checks if the grid elements have been moved
	 */
	private isGridItemsHasMoved(layout: KtdGridLayout): boolean {
		let itemHaveBeenMoved = false;

		this.currentGrid.forEach((currentGridItem) => {
			const gridItemFound = layout.find((newGridItem) => {
				return currentGridItem.id === newGridItem.id;
			});

			if (gridItemFound && GridItemUtils.isItemHaveBeenMoved(currentGridItem, gridItemFound)) {
				itemHaveBeenMoved = true;
			}
		});

		return itemHaveBeenMoved;
	}

	/**
	 * Get unique expression for tracking grid items.
	 * Track all properties of the grid item to ensure it will be re-rendered if any property change.
	 *
	 * @param gridItem The grid item
	 */
	public getGridItemTrackExpression(gridItem: KtdGridLayoutItem): string {
		return gridItem.id + '-' + gridItem.x + '-' + gridItem.y + '-' + gridItem.w + '-' + gridItem.h;
	}
}
