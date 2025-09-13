import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';

import { App } from './app';

describe('App', () => {
	let component: App;
	let fixture: ComponentFixture<App>;

	beforeEach(async () => {
		await TestBed.configureTestingModule({
			imports: [App],
			providers: [provideHttpClient(withInterceptorsFromDi()), provideHttpClientTesting()]
		}).compileComponents();

		fixture = TestBed.createComponent(App);
		component = fixture.componentInstance;
		fixture.detectChanges();
	});

	it('should create', () => {
		expect(component).toBeTruthy();
	});
});
