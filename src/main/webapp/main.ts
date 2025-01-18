import { enableProdMode, importProvidersFrom } from '@angular/core';
import { bootstrapApplication } from '@angular/platform-browser';

import { AppComponent } from './app/app.component';
import { DashboardModule } from './app/dashboard/dashboard.module';
import { SharedModule } from './app/shared/shared.module';
import { environment } from './environments/environment';

if (environment.production) {
  enableProdMode();
}

bootstrapApplication(AppComponent, {
  providers: [importProvidersFrom(DashboardModule, SharedModule)]
}).catch((err) => console.error(err));
