import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {MatButtonModule} from "@angular/material/button";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatDividerModule} from "@angular/material/divider";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatIconModule} from "@angular/material/icon";
import {HttpClientModule} from "@angular/common/http";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {SafeHtmlPipe} from "./pipes/safe-html/safe-html.pipe";
import { WidgetConfigurationComponent } from './components/widget-configuration/widget-configuration.component';
import { DashboardScreenComponent } from './components/dashboard-screen/dashboard-screen.component';
import {NgGridModule} from "angular2-grid";
import { DashboardScreenWidgetComponent } from './components/dashboard-screen/dashboard-screen-widget/dashboard-screen-widget.component';

@NgModule({
  declarations: [
    AppComponent,
    SafeHtmlPipe,
    WidgetConfigurationComponent,
    DashboardScreenComponent,
    DashboardScreenWidgetComponent
  ],
  imports: [
    MatIconModule,
    MatButtonModule,
    MatDividerModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatProgressSpinnerModule,
    NgGridModule,
    HttpClientModule,
    AppRoutingModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
