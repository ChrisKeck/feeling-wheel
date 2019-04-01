import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {JhiLanguageHelper} from 'app/core';

import {EfwgatewaySharedModule} from 'app/shared';
import {JhiLanguageService} from 'ng-jhipster';
import {
    FeelingIsoComponent,
    FeelingIsoDeleteDialogComponent,
    FeelingIsoDeletePopupComponent,
    FeelingIsoDetailComponent,
    FeelingIsoUpdateComponent,
    feelingPopupRoute,
    feelingRoute
} from './';

const ENTITY_STATES = [...feelingRoute, ...feelingPopupRoute];

@NgModule({
              imports: [EfwgatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
              declarations: [FeelingIsoComponent,
                             FeelingIsoDetailComponent,
                             FeelingIsoUpdateComponent,
                             FeelingIsoDeleteDialogComponent,
                             FeelingIsoDeletePopupComponent],
              entryComponents: [FeelingIsoComponent, FeelingIsoUpdateComponent, FeelingIsoDeleteDialogComponent, FeelingIsoDeletePopupComponent],
              providers: [{provide: JhiLanguageService, useClass: JhiLanguageService}],
              schemas: [CUSTOM_ELEMENTS_SCHEMA]
          })
export class EfwserviceFeelingIsoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
