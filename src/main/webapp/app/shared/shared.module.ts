/*
 * Copyright 2012-2021 the original author or authors.
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

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { SafeHtmlPipe } from './pipes/safe-html/safe-html.pipe';
import { AppRoutingModule } from '../app-routing.module';
import { WidgetHtmlDirective } from './directives/widget-html.directive';
import { SpinnerComponent } from './components/spinner/spinner.component';
import { MatProgressSpinnerModule } from "@angular/material/progress-spinner";
import { KtdGridModule } from "@katoid/angular-grid-layout";
import {MaterialModule} from "./modules/material.module";
import {NgOptimizedImage} from "@angular/common";

@NgModule({
  imports: [
    MaterialModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    KtdGridModule,
    NgOptimizedImage
  ],
  declarations: [SpinnerComponent, SafeHtmlPipe, WidgetHtmlDirective],
  exports: [
    MaterialModule,
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    KtdGridModule,
    SpinnerComponent,
    SafeHtmlPipe,
    WidgetHtmlDirective,
    NgOptimizedImage
  ]
})
export class SharedModule {}
