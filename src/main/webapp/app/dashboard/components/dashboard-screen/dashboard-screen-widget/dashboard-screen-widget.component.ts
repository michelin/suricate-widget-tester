import { NgClass } from '@angular/common';
import { Component, Input, OnInit } from '@angular/core';

import { SpinnerComponent } from '../../../../shared/components/spinner/spinner.component';
import { WidgetHtmlDirective } from '../../../../shared/directives/widget-html.directive';
import { ProjectWidget } from '../../../../shared/models/project-widget/project-widget';
import { SafeHtmlPipe } from '../../../../shared/pipes/safe-html/safe-html.pipe';
import { LibraryService } from '../../../services/library/library.service';

@Component({
  selector: 'suricate-dashboard-screen-widget',
  templateUrl: './dashboard-screen-widget.component.html',
  styleUrls: ['./dashboard-screen-widget.component.scss'],
  imports: [NgClass, SpinnerComponent, WidgetHtmlDirective, SafeHtmlPipe]
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
