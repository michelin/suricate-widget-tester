import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProjectWidget } from '../../../../shared/models/backend/project-widget/project-widget';
import { DashboardScreenWidget } from './dashboard-screen-widget';

describe('DashboardScreenWidget', () => {
	let component: DashboardScreenWidget;
	let fixture: ComponentFixture<DashboardScreenWidget>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [DashboardScreenWidget]
		}).compileComponents();

		fixture = TestBed.createComponent(DashboardScreenWidget);
		component = fixture.componentInstance;
		fixture.componentRef.setInput('projectWidget', buildMockedProjectWidget());
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
