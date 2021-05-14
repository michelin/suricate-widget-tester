import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardScreenWidgetComponent } from './dashboard-screen-widget.component';
import {SafeHtmlPipe} from '../../../pipes/safe-html/safe-html.pipe';

describe('DashboardScreenWidgetComponent', () => {
  let component: DashboardScreenWidgetComponent;
  let fixture: ComponentFixture<DashboardScreenWidgetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DashboardScreenWidgetComponent, SafeHtmlPipe ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DashboardScreenWidgetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
