import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { DashboardModule } from './dashboard/dashboard.module';
import { SharedModule } from './shared/shared.module';

@NgModule({
  imports: [DashboardModule, SharedModule],
  declarations: [AppComponent],
  bootstrap: [AppComponent]
})
export class AppModule {}
