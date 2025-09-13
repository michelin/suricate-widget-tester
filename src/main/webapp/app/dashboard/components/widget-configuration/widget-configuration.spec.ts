import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WidgetConfiguration } from './widget-configuration';

describe('WidgetConfiguration', () => {
	let component: WidgetConfiguration;
	let fixture: ComponentFixture<WidgetConfiguration>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [WidgetConfiguration],
			providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
		}).compileComponents();

		fixture = TestBed.createComponent(WidgetConfiguration);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
