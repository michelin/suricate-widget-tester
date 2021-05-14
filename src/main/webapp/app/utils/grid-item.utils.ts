/**
 * Class that manage the utility function for grid items management
 */
import {NgGridItemConfig} from "angular2-grid";

export class GridItemUtils {

  /**
   * Check if item have been moved
   *
   * @param beforeEvent Item state before move action
   * @param afterEvent Item state after move action
   */
  public static isItemHaveBeenMoved(beforeEvent: NgGridItemConfig, afterEvent: NgGridItemConfig) {
    return (
      beforeEvent.sizex !== afterEvent.sizex ||
      beforeEvent.sizey !== afterEvent.sizey ||
      beforeEvent.col !== afterEvent.col ||
      beforeEvent.row !== afterEvent.row
    );
  }
}
