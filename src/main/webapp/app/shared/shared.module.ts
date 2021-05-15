/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {NgGridModule} from 'angular2-grid';
import {NgModule} from '@angular/core';

import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {BrowserModule} from '@angular/platform-browser';
import {SafeHtmlPipe} from './pipes/safe-html/safe-html.pipe';
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {MatDividerModule} from "@angular/material/divider";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatProgressSpinnerModule} from "@angular/material/progress-spinner";
import {AppRoutingModule} from "../app-routing.module";
import {WidgetHtmlDirective} from "./directives/widget-html.directive";
import {SpinnerComponent} from "./components/spinner/spinner.component";

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
    HttpClientModule,
    AppRoutingModule
  ],
  declarations: [
    SpinnerComponent,
    SafeHtmlPipe,
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
    HttpClientModule,
    AppRoutingModule,
    SpinnerComponent,
    SafeHtmlPipe,
    WidgetHtmlDirective
  ],
  providers: [  ]
})
export class SharedModule {}
