import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {FontAwesomeModule} from '@fortawesome/angular-fontawesome';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NgJhipsterModule} from 'ng-jhipster';
import {CookieModule} from 'ngx-cookie';
import {InfiniteScrollModule} from 'ngx-infinite-scroll';

@NgModule({
              imports: [NgbModule.forRoot(), InfiniteScrollModule, CookieModule.forRoot(), FontAwesomeModule],
              exports: [FormsModule, CommonModule, NgbModule, NgJhipsterModule, InfiniteScrollModule, FontAwesomeModule]
          })
export class EfwgatewaySharedLibsModule {
    static forRoot() {
        return {
            ngModule: EfwgatewaySharedLibsModule
        };
    }
}
