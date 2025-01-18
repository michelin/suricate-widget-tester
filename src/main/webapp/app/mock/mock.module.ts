/*
 *  /*
 *  * Copyright 2012-2021 the original author or authors.
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

import { ElementRef, NgModule } from '@angular/core';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MockElementRef } from './models/mock-element-ref';
import { SharedModule } from '../shared/shared.module';
import {DashboardModule} from "../dashboard/dashboard.module";
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';

@NgModule({ exports: [SharedModule, DashboardModule, HttpClientTestingModule, RouterTestingModule], imports: [SharedModule, DashboardModule, RouterTestingModule], providers: [{ provide: ElementRef, useClass: MockElementRef }, provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()] })
export class MockModule {}
