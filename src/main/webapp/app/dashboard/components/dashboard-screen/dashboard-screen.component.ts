import { Component, ElementRef, Input, OnChanges, OnInit, Renderer2, SimpleChanges, ViewChild } from '@angular/core';
import { NgGridConfig, NgGridItemConfig } from 'angular2-grid';
import { ProjectWidget } from '../../../shared/models/project-widget/project-widget';
import { LibraryService } from '../../services/library/library.service';
import { HttpLibraryService } from '../../../shared/services/backend/http-library/http-library.service';
import { WidgetExecutionResult } from '../../../shared/models/widget-execution/widget-execution-result/widget-execution-result';
import { GridItemUtils } from '../../../shared/utils/grid-item.utils';

@Component({
  selector: 'suricate-dashboard-screen',
  templateUrl: './dashboard-screen.component.html',
  styleUrls: ['./dashboard-screen.component.scss']
})
export class DashboardScreenComponent implements OnInit, OnChanges {
  /**
   * Reference on the span containing all the required JS libraries
   */
  @ViewChild('externalJsLibraries')
  public externalJsLibrariesSpan!: ElementRef<HTMLSpanElement>;

  /**
   * The widget execution result
   */
  @Input()
  public widgetExecutionResult: WidgetExecutionResult | undefined;

  /**
   * The path of the widget folder
   */
  @Input()
  public widgetPath: string | undefined;

  /**
   * The options for the plugin angular2-grid
   */
  public gridOptions: NgGridConfig = {};

  /**
   * The grid items description
   */
  public gridStackItems: NgGridItemConfig[] = [];

  /**
   * Grid state when widgets were first loaded
   */
  protected startGridStackItems: NgGridItemConfig[] = [];

  /**
   * Grid configuration of the widget
   */
  private widgetGridConfig: NgGridItemConfig = { col: 0, row: 0, sizey: 1, sizex: 1 };

  /**
   * Constructor
   *
   * @param renderer The renderer Angular entity
   * @param libraryService Front-End service used to manage the libraries
   */
  constructor(private renderer: Renderer2, private readonly libraryService: LibraryService) {}

  /**
   * Init method
   */
  ngOnInit(): void {}

  /**
   * Changes method
   *
   * @param changes The changes event
   */
  ngOnChanges(changes: SimpleChanges): void {
    if (changes.widgetExecutionResult) {
      if (!changes.widgetExecutionResult.previousValue) {
        // Inject this variable in the window scope because some widgets use it to init the js
        (window as any)['page_loaded'] = true;
      }

      this.initGridStackOptions();
    }

    this.initGridStackItems();
    this.addExternalJSLibrariesToTheDOM();
  }

  /**
   * Create the list of gridStackItems used to display widgets on the grid
   */
  private initGridStackItems(): void {
    this.gridStackItems = [];

    if (this.widgetExecutionResult && this.widgetExecutionResult.projectWidget) {
      this.startGridStackItems = this.getGridStackItemsFromProjectWidgets(this.widgetExecutionResult.projectWidget);

      // Make a copy with a new reference
      this.gridStackItems = JSON.parse(JSON.stringify(this.startGridStackItems));
    }
  }

  /**
   * Init the options for Grid Stack plugin
   */
  private initGridStackOptions(): void {
    this.gridOptions = {
      visible_cols: 5,
      min_cols: 1,
      row_height: 360,
      min_rows: 1,
      margins: [4],
      auto_resize: true,
      draggable: true,
      resizable: true
    };
  }

  /**
   * Get the list of GridItemConfigs from project widget
   *
   * @param projectWidget The project widgets
   */
  private getGridStackItemsFromProjectWidgets(projectWidget: ProjectWidget): NgGridItemConfig[] {
    const gridStackItemsConfig: NgGridItemConfig[] = [];

    if (this.widgetExecutionResult?.projectWidget) {
      gridStackItemsConfig.push({
        col: this.widgetGridConfig.col,
        row: this.widgetGridConfig.row,
        sizey: this.widgetGridConfig.sizey,
        sizex: this.widgetGridConfig.sizex,
        payload: this.widgetExecutionResult.projectWidget
      });
    }

    return gridStackItemsConfig;
  }

  /**
   * Update the project widget position
   */
  public updateProjectWidgetsPosition(): void {
    if (this.isGridItemsHasMoved()) {
      this.widgetGridConfig = this.gridStackItems[0];

      this.initGridStackItems();
    }
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
   * Checks if the grid elements have been moved
   */
  private isGridItemsHasMoved(): boolean {
    let itemHaveBeenMoved = false;

    this.startGridStackItems.forEach(startGridItem => {
      if (this.gridStackItems[0] && GridItemUtils.isItemHaveBeenMoved(startGridItem, this.gridStackItems[0])) {
        itemHaveBeenMoved = true;
      }
    });

    return itemHaveBeenMoved;
  }
}
