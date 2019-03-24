import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { EfwgatewaySharedModule } from 'app/shared';
import {
    EmployeeIsoComponent,
    EmployeeIsoDetailComponent,
    EmployeeIsoUpdateComponent,
    EmployeeIsoDeletePopupComponent,
    EmployeeIsoDeleteDialogComponent,
    employeeRoute,
    employeePopupRoute
} from './';

const ENTITY_STATES = [...employeeRoute, ...employeePopupRoute];

@NgModule({
    imports: [EfwgatewaySharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        EmployeeIsoComponent,
        EmployeeIsoDetailComponent,
        EmployeeIsoUpdateComponent,
        EmployeeIsoDeleteDialogComponent,
        EmployeeIsoDeletePopupComponent
    ],
    entryComponents: [EmployeeIsoComponent, EmployeeIsoUpdateComponent, EmployeeIsoDeleteDialogComponent, EmployeeIsoDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EfwserviceEmployeeIsoModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
