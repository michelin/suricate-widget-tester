/**
 * Transform a string into a SafeHtml
 */
import {Pipe, PipeTransform} from "@angular/core";
import {DomSanitizer, SafeHtml} from "@angular/platform-browser";

@Pipe({
  name: 'safeHtml'
})
export class SafeHtmlPipe implements PipeTransform {
  /**
   * The constructor
   * @param {DomSanitizer} domSanitizer The dom sanitizer service
   */
  constructor(private readonly domSanitizer: DomSanitizer) {}

  /**
   * The transform function
   *
   * @param {string} valueToSanitize The string value to sanitize
   * @returns {SafeHtml}
   */
  public transform(valueToSanitize: string): SafeHtml {
    return this.domSanitizer.bypassSecurityTrustHtml(valueToSanitize);
  }
}
