/**
 * Transform a string into a SafeHtml
 */
import { inject, Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Pipe({
	name: 'safeHtml',
	standalone: true
})
export class SafeHtmlPipe implements PipeTransform {
	private readonly domSanitizer = inject(DomSanitizer);

	/**
	 * The transform function
	 *
	 * @param valueToSanitize The string value to sanitize
	 */
	public transform(valueToSanitize: string): SafeHtml {
		return this.domSanitizer.bypassSecurityTrustHtml(valueToSanitize);
	}
}
