import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashboardScreen } from './dashboard-screen';

describe('DashboardScreen', () => {
	let component: DashboardScreen;
	let fixture: ComponentFixture<DashboardScreen>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [DashboardScreen]
		}).compileComponents();

		fixture = TestBed.createComponent(DashboardScreen);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
