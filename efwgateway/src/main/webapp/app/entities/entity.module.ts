import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'employee-iso',
                loadChildren: './efwservice/employee-iso/employee-iso.module#EfwserviceEmployeeIsoModule'
            },
            {
                path: 'feeling-iso',
                loadChildren: './efwservice/feeling-iso/feeling-iso.module#EfwserviceFeelingIsoModule'
            },
            {
                path: 'feel-wheel-iso',
                loadChildren: './efwservice/feel-wheel-iso/feel-wheel-iso.module#EfwserviceFeelWheelIsoModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EfwgatewayEntityModule {}
