import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardScreenComponent } from './dashboard-screen.component';

describe('WidgetResponseComponent', () => {
  let component: DashboardScreenComponent;
  let fixture: ComponentFixture<DashboardScreenComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DashboardScreenComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(DashboardScreenComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
