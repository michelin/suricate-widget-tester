import {Component, Input, OnInit} from '@angular/core';
import {ProjectWidget} from '../../../models/project-widget/project-widget';
import {NgGridItemConfig, NgGridItemEvent} from 'angular2-grid';
import {GridItemUtils} from '../../../utils/grid-item.utils';

@Component({
  selector: 'suricate-dashboard-screen-widget',
  templateUrl: './dashboard-screen-widget.component.html',
  styleUrls: ['./dashboard-screen-widget.component.scss']
})
export class DashboardScreenWidgetComponent implements OnInit {

  /**
   * The projectWidget to display
   */
  @Input()
  public projectWidget?: ProjectWidget;

  /**
   * The grid item config
   */
  @Input()
  public gridStackItem!: NgGridItemConfig;

  /**
   * The configuration of this project widget on the grid
   */
  private startGridStackItem!: NgGridItemConfig;

  /**
   * Constructor
   */
  constructor() { }

  /**
   * Init method
   */
  ngOnInit(): void {
    this.startGridStackItem = { ...this.gridStackItem };
  }

  /**
   * Register the new position of the element
   *
   * @param gridItemEvent The grid item event
   */
  public registerNewPosition(gridItemEvent: NgGridItemEvent): void {
    this.gridStackItem.col = gridItemEvent.col;
    this.gridStackItem.row = gridItemEvent.row;
    this.gridStackItem.sizey = gridItemEvent.sizey;
    this.gridStackItem.sizex = gridItemEvent.sizex;
  }

  /**
   * Disable click event if the item have been moved
   * @param event The click event
   */
  public preventDefault(event: MouseEvent): void {
    if (GridItemUtils.isItemHaveBeenMoved(this.startGridStackItem, this.gridStackItem)) {
      event.preventDefault();
    }
  }
}
