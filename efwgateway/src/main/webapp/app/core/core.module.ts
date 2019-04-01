import {DatePipe, registerLocaleData} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import locale from '@angular/common/locales/de';
import {LOCALE_ID, NgModule} from '@angular/core';
import {Title} from '@angular/platform-browser';

@NgModule({
              imports: [HttpClientModule], exports: [], declarations: [], providers: [Title, {
        provide: LOCALE_ID, useValue: 'de'
    }, DatePipe]
          })
export class EfwgatewayCoreModule {
    constructor() {
        registerLocaleData(locale);
    }
}
