import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WidgetConfigurationComponent } from './widget-configuration.component';

describe('WidgetConfigurationComponent', () => {
  let component: WidgetConfigurationComponent;
  let fixture: ComponentFixture<WidgetConfigurationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
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
