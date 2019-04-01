import {Routes} from '@angular/router';

import {UserRouteAccessService} from 'app/core';

import {auditsRoute, configurationRoute, docsRoute, gatewayRoute, healthRoute, logsRoute, metricsRoute, trackerRoute, userMgmtRoute} from './';

const ADMIN_ROUTES = [auditsRoute, configurationRoute, docsRoute, healthRoute, logsRoute, gatewayRoute, trackerRoute, ...userMgmtRoute, metricsRoute];

export const adminState: Routes = [{
    path: '', data: {
        authorities: ['ROLE_ADMIN']
    }, canActivate: [UserRouteAccessService], children: ADMIN_ROUTES
}];
