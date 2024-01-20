import { Component, ElementRef, Input, OnChanges, Renderer2, SimpleChanges, ViewChild } from '@angular/core';
import { LibraryService } from '../../services/library/library.service';
import { HttpLibraryService } from '../../../shared/services/backend/http-library/http-library.service';
import { WidgetExecutionResult } from '../../../shared/models/widget-execution/widget-execution-result/widget-execution-result';
import { GridItemUtils } from '../../../shared/utils/grid-item.utils';
import {GridOptions} from "../../../shared/models/grid/grid-options";
import {KtdGridLayout} from "@katoid/angular-grid-layout";

@Component({
  selector: 'suricate-dashboard-screen',
  templateUrl: './dashboard-screen.component.html',
  styleUrls: ['./dashboard-screen.component.scss']
})
export class DashboardScreenComponent implements OnChanges {
  /**
   * Reference on the span containing all the required JS libraries
   */
  @ViewChild('externalJsLibraries')
  public externalJsLibrariesSpan!: ElementRef<HTMLSpanElement>;

  /**
   * The widget execution result
   */
  @Input()
  public widgetExecutionResult: WidgetExecutionResult;

  /**
   * The path of the widget folder
   */
  @Input()
  public widgetPath: string;

  /**
   * The grid options
   */
  public gridOptions: GridOptions;

  /**
   * All the grids of the dashboard
   */
  public currentGrid: KtdGridLayout = [];

  /**
   * Constructor
   *
   * @param renderer The renderer Angular entity
   * @param libraryService Front-End service used to manage the libraries
   */
  constructor(private renderer: Renderer2, private readonly libraryService: LibraryService) {}

  /**
   * Changes method
   *
   * @param changes The changes event
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes.widgetExecutionResult) {
      if (!changes.widgetExecutionResult.previousValue) {
        // Inject this variable in the window scope because some widgets use it to init the js
        (window as any).page_loaded = true;
      }

      this.initGridStackOptions();
    }

    this.initGrid();
    this.addExternalJSLibrariesToTheDOM();
  }

  /**
   * Create the list of grid items used to display widgets on the grids
   */
  private initGrid(): void {
    this.currentGrid = [];
    if (this.widgetExecutionResult && this.widgetExecutionResult.projectWidget) {
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
      id: String(this.widgetExecutionResult.projectWidget.id),
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
   */
  public addExternalJSLibrariesToTheDOM(): void {
    if (this.widgetExecutionResult && this.widgetExecutionResult.projectWidget) {
      if (this.widgetExecutionResult.projectWidget.librariesNames) {
        this.libraryService.numberOfExternalLibrariesToLoad = this.widgetExecutionResult.projectWidget.librariesNames.length;

        const widgetFolderPath = this.widgetExecutionResult.widgetExecutionRequest.path;

        this.widgetExecutionResult.projectWidget.librariesNames.forEach(libraryName => {
          const script: HTMLScriptElement = document.createElement('script');
          script.type = 'text/javascript';
          script.src = HttpLibraryService.getContentUrl(libraryName, encodeURIComponent(widgetFolderPath));
          script.onload = () => this.libraryService.markScriptAsLoaded(libraryName);
          script.async = false;

          this.renderer.appendChild(this.externalJsLibrariesSpan.nativeElement, script);
        });
      }

      // No library to load
      else {
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

    this.currentGrid.forEach(currentGridItem => {
      const gridItemFound = layout.find(newGridItem => {
        return currentGridItem.id === newGridItem.id;
      });

      if (gridItemFound && GridItemUtils.isItemHaveBeenMoved(currentGridItem, gridItemFound)) {
        itemHaveBeenMoved = true;
      }
    });

    return itemHaveBeenMoved;
  }
}
