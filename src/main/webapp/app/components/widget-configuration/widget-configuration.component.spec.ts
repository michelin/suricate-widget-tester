import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WidgetConfigurationComponent } from './widget-configuration.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientTestingModule} from '@angular/common/http/testing';

describe('WidgetConfigurationComponent', () => {
  let component: WidgetConfigurationComponent;
  let fixture: ComponentFixture<WidgetConfigurationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ FormsModule, ReactiveFormsModule, HttpClientTestingModule ],
      declarations: [ WidgetConfigurationComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(WidgetConfigurationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
