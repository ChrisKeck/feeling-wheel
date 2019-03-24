import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { EfwgatewaySharedModule } from 'app/shared';
import {
    FeelingIsoComponent,
    FeelingIsoDetailComponent,
    FeelingIsoUpdateComponent,
    FeelingIsoDeletePopupComponent,
    FeelingIsoDeleteDialogComponent,
    feelingRoute,
    feelingPopupRoute
} from './';

const ENTITY_STATES = [...feelingRoute, ...feelingPopupRoute];

@NgModule({
    imports: [EfwgatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FeelingIsoComponent,
        FeelingIsoDetailComponent,
        FeelingIsoUpdateComponent,
        FeelingIsoDeleteDialogComponent,
        FeelingIsoDeletePopupComponent
    ],
    entryComponents: [FeelingIsoComponent, FeelingIsoUpdateComponent, FeelingIsoDeleteDialogComponent, FeelingIsoDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
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
