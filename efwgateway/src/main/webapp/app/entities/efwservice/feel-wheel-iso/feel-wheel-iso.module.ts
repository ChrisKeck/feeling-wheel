import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { EfwgatewaySharedModule } from 'app/shared';
import {
    FeelWheelIsoComponent,
    FeelWheelIsoDetailComponent,
    FeelWheelIsoUpdateComponent,
    FeelWheelIsoDeletePopupComponent,
    FeelWheelIsoDeleteDialogComponent,
    feelWheelRoute,
    feelWheelPopupRoute
} from './';

const ENTITY_STATES = [...feelWheelRoute, ...feelWheelPopupRoute];

@NgModule({
    imports: [EfwgatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FeelWheelIsoComponent,
        FeelWheelIsoDetailComponent,
        FeelWheelIsoUpdateComponent,
        FeelWheelIsoDeleteDialogComponent,
        FeelWheelIsoDeletePopupComponent
    ],
    entryComponents: [
        FeelWheelIsoComponent,
        FeelWheelIsoUpdateComponent,
        FeelWheelIsoDeleteDialogComponent,
        FeelWheelIsoDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EfwserviceFeelWheelIsoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
