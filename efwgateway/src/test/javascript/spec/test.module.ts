import {DatePipe} from '@angular/common';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {ElementRef, NgModule, Renderer} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {AccountService, JhiLanguageHelper, JhiTrackerService, LoginModalService} from 'app/core';
import {JhiAlertService, JhiDataUtils, JhiDateUtils, JhiEventManager, JhiLanguageService, JhiParseLinks} from 'ng-jhipster';
import {MockAccountService} from './helpers/mock-account.service';
import {MockActiveModal} from './helpers/mock-active-modal.service';
import {MockEventManager} from './helpers/mock-event-manager.service';

import {MockLanguageHelper, MockLanguageService} from './helpers/mock-language.service';
import {MockActivatedRoute, MockRouter} from './helpers/mock-route.service';

@NgModule({
              providers: [DatePipe, JhiDataUtils, JhiDateUtils, JhiParseLinks, {
                  provide: JhiLanguageService, useClass: MockLanguageService
              }, {
                  provide: JhiLanguageHelper, useClass: MockLanguageHelper
              }, {
                  provide: JhiTrackerService, useValue: null
              }, {
                  provide: JhiEventManager, useClass: MockEventManager
              }, {
                  provide: NgbActiveModal, useClass: MockActiveModal
              }, {
                  provide: ActivatedRoute, useValue: new MockActivatedRoute({id: 123})
              }, {
                  provide: Router, useClass: MockRouter
              }, {
                  provide: AccountService, useClass: MockAccountService
              }, {
                  provide: LoginModalService, useValue: null
              }, {
                  provide: ElementRef, useValue: null
              }, {
                  provide: Renderer, useValue: null
              }, {
                  provide: JhiAlertService, useValue: null
              }, {
                  provide: NgbModal, useValue: null
              }], imports: [HttpClientTestingModule]
          })
export class EfwgatewayTestModule {
}
