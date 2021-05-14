import {AfterViewInit, Component, ElementRef, Input, OnChanges, OnInit, SimpleChanges, ViewChild} from '@angular/core';
import {NgGridConfig, NgGridItemConfig} from "angular2-grid";
import {ProjectWidget} from "../../models/project-widget/project-widget";

@Component({
  selector: 'suricate-dashboard-screen',
  templateUrl: './dashboard-screen.component.html',
  styleUrls: ['./dashboard-screen.component.scss']
})
export class DashboardScreenComponent implements OnInit, OnChanges, AfterViewInit {

  /**
   * The project widget list
   */
  @Input()
  public projectWidgets!: ProjectWidget[];

  /**
   * The error message that can occur during the
   */
  @Input()
  public widgetExecutionErrorMessage: string | undefined;

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
   * Constructor
   */
  constructor() { }

  /**
   * Init method
   */
  ngOnInit(): void {
  }

  /**
   * Changes method
   *
   * @param changes
   */
  ngOnChanges(changes: SimpleChanges): void {
    this.initGridStackOptions();
    this.initGridStackItems()
  }

  /**
   * After view init method
   */
  public ngAfterViewInit(): void {
    this.addExternalJSLibrariesToTheDOM();
  }

  /**
   * Create the list of gridStackItems used to display widgets on the grid
   */
  private initGridStackItems(): void {
    this.gridStackItems = [];

    if (this.projectWidgets) {
      this.startGridStackItems = this.getGridStackItemsFromProjectWidgets(this.projectWidgets);
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
   * @param projectWidgets The project widgets
   */
  private getGridStackItemsFromProjectWidgets(projectWidgets: ProjectWidget[]): NgGridItemConfig[] {
    const gridStackItemsConfig: NgGridItemConfig[] = [];

    if (this.projectWidgets && this.projectWidgets[0]) {
      console.warn(this.projectWidgets)
      gridStackItemsConfig.push({
        col: 0,
        row: 0,
        sizey: 1,
        sizex: 1,
        payload: this.projectWidgets[0]
      });
    }

    return gridStackItemsConfig;
  }

  /**
   * For each JS libraries linked with the project, create a script element with the URL of the library
   * and a callback which notify subscribers when the library is loaded.
   */
  public addExternalJSLibrariesToTheDOM() {

  }
}
