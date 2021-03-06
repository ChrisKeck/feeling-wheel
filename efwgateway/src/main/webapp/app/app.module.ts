import {HTTP_INTERCEPTORS} from '@angular/common/http';

import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {NgbDatepickerConfig} from '@ng-bootstrap/ng-bootstrap';
import {EfwgatewayCoreModule} from 'app/core';
import {EfwgatewaySharedModule} from 'app/shared';
import * as moment from 'moment';
import {NgJhipsterModule} from 'ng-jhipster';
import {Ng2Webstorage} from 'ngx-webstorage';
import {EfwgatewayAccountModule} from './account/account.module';
import {EfwgatewayAppRoutingModule} from './app-routing.module';
import {AuthExpiredInterceptor} from './blocks/interceptor/auth-expired.interceptor';

import {AuthInterceptor} from './blocks/interceptor/auth.interceptor';
import {ErrorHandlerInterceptor} from './blocks/interceptor/errorhandler.interceptor';
import {NotificationInterceptor} from './blocks/interceptor/notification.interceptor';
import {EfwgatewayEntityModule} from './entities/entity.module';
import {EfwgatewayHomeModule} from './home/home.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import {ActiveMenuDirective, ErrorComponent, FooterComponent, JhiMainComponent, NavbarComponent, PageRibbonComponent} from './layouts';
import './vendor.ts';

@NgModule({
              imports: [BrowserModule,
                        Ng2Webstorage.forRoot({prefix: 'jhi', separator: '-'}),
                        NgJhipsterModule.forRoot({
                                                     // set below to true to make alerts look like toast
                                                     alertAsToast: false,
                                                     alertTimeout: 5000,
                                                     i18nEnabled: true,
                                                     defaultI18nLang: 'de'
                                                 }),
                        EfwgatewaySharedModule.forRoot(),
                        EfwgatewayCoreModule,
                        EfwgatewayHomeModule,
                        EfwgatewayAccountModule,
                  // jhipster-needle-angular-add-module JHipster will add new module here
                        EfwgatewayEntityModule,
                        EfwgatewayAppRoutingModule],
              declarations: [JhiMainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
              providers: [{
                  provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true
              }, {
                  provide: HTTP_INTERCEPTORS, useClass: AuthExpiredInterceptor, multi: true
              }, {
                  provide: HTTP_INTERCEPTORS, useClass: ErrorHandlerInterceptor, multi: true
              }, {
                  provide: HTTP_INTERCEPTORS, useClass: NotificationInterceptor, multi: true
              }],
              bootstrap: [JhiMainComponent]
          })
export class EfwgatewayAppModule {
    constructor(private dpConfig: NgbDatepickerConfig) {
        this.dpConfig.minDate = {year: moment().year() - 100, month: 1, day: 1};
    }
}
