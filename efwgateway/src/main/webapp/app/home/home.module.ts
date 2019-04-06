import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {EntitiesQuickSelectionComponent} from 'app/home/entities-quick-selection/entities-quick-selection.component';

import {EfwgatewaySharedModule} from 'app/shared';
import {HOME_ROUTE, HomeComponent} from './';

@NgModule({
              imports: [EfwgatewaySharedModule, RouterModule.forChild([HOME_ROUTE])],
              declarations: [HomeComponent, EntitiesQuickSelectionComponent],
              schemas: [CUSTOM_ELEMENTS_SCHEMA]
          })
export class EfwgatewayHomeModule {
}
