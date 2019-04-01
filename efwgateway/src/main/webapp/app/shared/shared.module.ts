import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {NgbDateAdapter} from '@ng-bootstrap/ng-bootstrap';
import {EfwgatewaySharedCommonModule, EfwgatewaySharedLibsModule, HasAnyAuthorityDirective, JhiLoginModalComponent} from './';

import {NgbDateMomentAdapter} from './util/datepicker-adapter';

@NgModule({
              imports: [EfwgatewaySharedLibsModule, EfwgatewaySharedCommonModule],
              declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
              providers: [{provide: NgbDateAdapter, useClass: NgbDateMomentAdapter}],
              entryComponents: [JhiLoginModalComponent],
              exports: [EfwgatewaySharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
              schemas: [CUSTOM_ELEMENTS_SCHEMA]
          })
export class EfwgatewaySharedModule {
    static forRoot() {
        return {
            ngModule: EfwgatewaySharedModule
        };
    }
}
