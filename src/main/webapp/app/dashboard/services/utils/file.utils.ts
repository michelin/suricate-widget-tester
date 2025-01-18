/*
 *  /*
 *  * Copyright 2012-2021 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

import { Observable, Observer } from 'rxjs';

/**
 * Utils class for images
 */
export class FileUtils {
  /**
   * Convert a file into base 64
   *
   * @param file The file to convert
   */
  public static convertFileToBase64(file: File): Observable<string | ArrayBuffer> {
    return new Observable((observer: Observer<string | ArrayBuffer>) => {
      const fileReader = new FileReader();

      fileReader.onerror = (err: ProgressEvent<FileReader>) => observer.error(err);
      fileReader.onabort = (err: ProgressEvent<FileReader>) => observer.error(err);
      fileReader.onload = () => {
        if (fileReader.result) {
          observer.next(fileReader.result);
        }
      };
      fileReader.onloadend = () => observer.complete();

      fileReader.readAsDataURL(file);

      return () => {
        fileReader.abort();
      };
    });
  }

  /**
   * Test if the base 64 url is an image
   * @param base64Url The base 64 url to test
   */
  public static isBase64UrlIsAnImage(base64Url: string) {
    const base64ImagePattern = '^data:image/(gif|jpe?g|png);base64,.+$';
    const regexp = new RegExp(base64ImagePattern);

    return regexp.test(base64Url);
  }
}
