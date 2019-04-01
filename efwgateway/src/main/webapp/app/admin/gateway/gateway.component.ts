import {Component, OnInit} from '@angular/core';
import {GatewayRoute} from './gateway-route.model';

import {GatewayRoutesService} from './gateway-routes.service';

@Component({
               selector: 'jhi-gateway', templateUrl: './gateway.component.html', providers: [GatewayRoutesService]
           })
export class JhiGatewayComponent implements OnInit {
    gatewayRoutes: GatewayRoute[];
    updatingRoutes: boolean;

    constructor(private gatewayRoutesService: GatewayRoutesService) {
    }

    ngOnInit() {
        this.refresh();
    }

    refresh() {
        this.updatingRoutes = true;
        this.gatewayRoutesService.findAll().subscribe(gatewayRoutes => {
            this.gatewayRoutes = gatewayRoutes;
            this.updatingRoutes = false;
        });
    }
}
