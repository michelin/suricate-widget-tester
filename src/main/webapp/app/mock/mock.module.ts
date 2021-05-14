/*
 *  /*
 *  * Copyright 2012-2018 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

import {ElementRef, NgModule} from '@angular/core';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {RouterTestingModule} from '@angular/router/testing';
import {MockElementRef} from './models/mock-element-ref';
import {NgGrid, NgGridModule} from 'angular2-grid';
import {SafeHtmlPipe} from '../pipes/safe-html/safe-html.pipe';
import {MatIconModule} from '@angular/material/icon';
import {MatButtonModule} from '@angular/material/button';
import {MatDividerModule} from '@angular/material/divider';
import {BrowserModule} from '@angular/platform-browser';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner';
import {AppComponent} from '../app.component';
import {WidgetConfigurationComponent} from '../components/widget-configuration/widget-configuration.component';
import {DashboardScreenComponent} from '../components/dashboard-screen/dashboard-screen.component';
import {DashboardScreenWidgetComponent} from '../components/dashboard-screen/dashboard-screen-widget/dashboard-screen-widget.component';
import {WidgetHtmlDirective} from '../directives/widget-html.directive';

@NgModule({
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
    HttpClientTestingModule,
    RouterTestingModule
  ],
  declarations: [
    AppComponent,
    SafeHtmlPipe,
    WidgetConfigurationComponent,
    DashboardScreenComponent,
    DashboardScreenWidgetComponent,
    WidgetHtmlDirective
  ],
  exports: [
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
    HttpClientTestingModule,
    RouterTestingModule,
    SafeHtmlPipe
  ],
  providers: [{ provide: ElementRef, useClass: MockElementRef }, NgGrid]
})
export class MockModule {}
