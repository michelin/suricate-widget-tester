/**
 * Class that manage the utility function for grid items management
 */
import {KtdGridLayoutItem} from "@katoid/angular-grid-layout";

export class GridItemUtils {
  /**
   * Check if item have been moved
   *
   * @param beforeEvent Item state before move action
   * @param afterEvent Item state after move action
   */
  public static isItemHaveBeenMoved(beforeEvent: KtdGridLayoutItem, afterEvent: KtdGridLayoutItem) {
    return (
      beforeEvent.w !== afterEvent.w ||
      beforeEvent.h !== afterEvent.h ||
      beforeEvent.x !== afterEvent.x ||
      beforeEvent.y !== afterEvent.y
    );
  }
}
