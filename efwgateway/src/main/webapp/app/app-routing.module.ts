import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {DEBUG_INFO_ENABLED} from 'app/app.constants';
import {errorRoute, navbarRoute} from './layouts';

const LAYOUT_ROUTES = [navbarRoute, ...errorRoute];

@NgModule({
              imports: [RouterModule.forRoot([{
                  path: 'admin', loadChildren: './admin/admin.module#EfwgatewayAdminModule'
              }, ...LAYOUT_ROUTES], {useHash: true, enableTracing: DEBUG_INFO_ENABLED})], exports: [RouterModule]
          })
export class EfwgatewayAppRoutingModule {
}
