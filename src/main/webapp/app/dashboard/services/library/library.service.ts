import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

/**
 * Service managing the external JS libraries used by the widgets
 */
@Injectable({ providedIn: 'root' })
export class LibraryService {
  /**
   * List of JS libraries to load for the current project
   */
  public loadedExternalLibraries: string[] = [];

  /**
   * Number of JS libraries to load for the current project
   */
  public numberOfExternalLibrariesToLoad = 0;

  /**
   * Are the required JS libraries loaded
   */
  public allExternalLibrariesLoaded = new BehaviorSubject<boolean>(false);

  /**
   * Emit a new event according to the given value
   *
   * @param value The new value to emit
   */
  public emitAreJSScriptsLoaded(value: boolean): void {
    this.allExternalLibrariesLoaded.next(value);
  }

  /**
   * Callback function called when a JS library has been injected in the DOM and loaded from the Back-End.
   * Mark the library linked with the given name as loaded.
   * Then, if all the required JS libraries have been loaded, then emit an event to the subscribers.
   *
   * @param libraryName A name representing a JS library
   */
  public markScriptAsLoaded(libraryName: string): void {
    if (this.loadedExternalLibraries.indexOf(libraryName) === -1) {
      this.loadedExternalLibraries.push(libraryName);
    }

    if (this.loadedExternalLibraries.length === this.numberOfExternalLibrariesToLoad) {
      this.emitAreJSScriptsLoaded(true);
    }
  }
}
