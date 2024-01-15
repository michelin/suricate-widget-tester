import { Component, Input, OnInit } from '@angular/core';
import { ProjectWidget } from '../../../../shared/models/project-widget/project-widget';
import { GridItemUtils } from '../../../../shared/utils/grid-item.utils';
import { LibraryService } from '../../../services/library/library.service';

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
  public projectWidget: ProjectWidget;

  /**
   * Is the widget loading or not
   */
  public loading = true;

  /**
   * Constructor
   */
  constructor(private readonly libraryService: LibraryService) {}

  /**
   * Init method
   */
  ngOnInit(): void {
    this.libraryService.allExternalLibrariesLoaded.subscribe((areExternalLibrariesLoaded: boolean) => {
      this.loading = !areExternalLibrariesLoaded;
    });
  }
}
