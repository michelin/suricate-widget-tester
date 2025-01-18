/**
 * Transform a string into a SafeHtml
 */
import { Pipe, PipeTransform } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

@Pipe({
  name: 'safeHtml',
  standalone: true
})
export class SafeHtmlPipe implements PipeTransform {
  /**
   * The constructor
   * @param domSanitizer The dom sanitizer service
   */
  constructor(private readonly domSanitizer: DomSanitizer) {}

  /**
   * The transform function
   *
   * @param valueToSanitize The string value to sanitize
   */
  public transform(valueToSanitize: string): SafeHtml {
    return this.domSanitizer.bypassSecurityTrustHtml(valueToSanitize);
  }
}
