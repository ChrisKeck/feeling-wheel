import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {JhiLanguageHelper} from 'app/core';
import {EfwgatewaySharedModule} from 'app/shared';
import {JhiLanguageService} from 'ng-jhipster';
import {
    adminState,
    AuditsComponent,
    JhiConfigurationComponent,
    JhiDocsComponent,
    JhiGatewayComponent,
    JhiHealthCheckComponent,
    JhiHealthModalComponent,
    JhiMetricsMonitoringComponent,
    JhiTrackerComponent,
    LogsComponent,
    UserMgmtComponent,
    UserMgmtDeleteDialogComponent,
    UserMgmtDetailComponent,
    UserMgmtUpdateComponent
} from './';

/* jhipster-needle-add-admin-module-import - JHipster will add admin modules imports here */

@NgModule({
              imports: [EfwgatewaySharedModule, RouterModule.forChild(adminState)
                  /* jhipster-needle-add-admin-module - JHipster will add admin modules here */],
              declarations: [AuditsComponent,
                             UserMgmtComponent,
                             UserMgmtDetailComponent,
                             UserMgmtUpdateComponent,
                             UserMgmtDeleteDialogComponent,
                             LogsComponent,
                             JhiConfigurationComponent,
                             JhiHealthCheckComponent,
                             JhiHealthModalComponent,
                             JhiDocsComponent,
                             JhiGatewayComponent,
                             JhiTrackerComponent,
                             JhiMetricsMonitoringComponent],
              providers: [{provide: JhiLanguageService, useClass: JhiLanguageService}],
              entryComponents: [UserMgmtDeleteDialogComponent, JhiHealthModalComponent],
              schemas: [CUSTOM_ELEMENTS_SCHEMA]
          })
export class EfwgatewayAdminModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
