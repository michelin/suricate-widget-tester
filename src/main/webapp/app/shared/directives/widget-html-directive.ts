import { Directive, ElementRef, inject, OnInit } from '@angular/core';

/**
 * Directive for Widget's JS scripts
 */
@Directive({
	selector: '[widgetHtmlDirective]',
	standalone: true
})
export class WidgetHtmlDirective implements OnInit {
	private readonly elementRef = inject(ElementRef);

	/**
	 * Init method
	 */
	ngOnInit(): void {
		this.reloadJSScripts();
	}

	/**
	 * From all the JS scripts contained by the current widget HTML section, build new scripts then insert them in the DOM.
	 * It executes the scripts again and render the widget properly.
	 * This is called once the HTML of the widget is fully loaded.
	 */
	private reloadJSScripts(): void {
		const scripts: HTMLScriptElement[] = (
			Array.from(this.elementRef.nativeElement.getElementsByTagName('script')) as HTMLScriptElement[]
		).filter((currentScript) => currentScript.src || currentScript.innerHTML);

		Array.from(Array(scripts.length).keys()).forEach((index: number) => {
			const script = scripts[index];

			const copyScript: HTMLScriptElement = document.createElement('script');
			copyScript.type = script.type ? script.type : 'text/javascript';
			copyScript.innerHTML = script.innerHTML;
			copyScript.async = false;

			if (script.parentNode != null) {
				script.parentNode.replaceChild(copyScript, script);
			}
		});
	}
}
