import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {DashboardModule} from "./dashboard/dashboard.module";
import {SharedModule} from "./shared/shared.module";

@NgModule({
  declarations: [AppComponent],
  imports: [
    DashboardModule,
    SharedModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
