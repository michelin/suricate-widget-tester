import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectWidget } from '../../../../shared/models/project-widget/project-widget';
import { DashboardScreenWidgetComponent } from './dashboard-screen-widget.component';

describe('DashboardScreenWidgetComponent', () => {
	let component: DashboardScreenWidgetComponent;
	let fixture: ComponentFixture<DashboardScreenWidgetComponent>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [DashboardScreenWidgetComponent]
		}).compileComponents();

		fixture = TestBed.createComponent(DashboardScreenWidgetComponent);
		component = fixture.componentInstance;
		component.projectWidget = buildMockedProjectWidget();

		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});

	function buildMockedProjectWidget(): ProjectWidget {
		return {
			id: 1,
			instantiateHtml: '',
			technicalName: '',
			cssContent: ''
		};
	}
});
