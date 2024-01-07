import { NgModule } from '@angular/core';
import { AppComponent } from './app.component';
import { DashboardModule } from './dashboard/dashboard.module';
import { SharedModule } from './shared/shared.module';
import { TestDirective } from './test.directive';

@NgModule({
  declarations: [AppComponent, TestDirective],
  imports: [DashboardModule, SharedModule],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
