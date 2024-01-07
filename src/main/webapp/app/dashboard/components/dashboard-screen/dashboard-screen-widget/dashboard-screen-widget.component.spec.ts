import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';
import { DashboardScreenWidgetComponent } from './dashboard-screen-widget.component';
import { MockModule } from '../../../../mock/mock.module';
import { MockedModelBuilderService } from '../../../../mock/services/mocked-model-builder/mocked-model-builder.service';

describe('DashboardScreenWidgetComponent', () => {
  let component: DashboardScreenWidgetComponent;
  let fixture: ComponentFixture<DashboardScreenWidgetComponent>;

  beforeEach(
    waitForAsync(() => {
      TestBed.configureTestingModule({
        imports: [MockModule],
        declarations: [DashboardScreenWidgetComponent]
      }).compileComponents();

      const mockedModelBuilderService = TestBed.inject(MockedModelBuilderService);

      fixture = TestBed.createComponent(DashboardScreenWidgetComponent);
      component = fixture.componentInstance;
      component.projectWidget = mockedModelBuilderService.buildMockedProjectWidget();

      fixture.detectChanges();
    })
  );

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
