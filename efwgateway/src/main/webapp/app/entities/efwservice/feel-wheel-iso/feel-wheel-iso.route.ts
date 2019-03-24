import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FeelWheelIso } from 'app/shared/model/efwservice/feel-wheel-iso.model';
import { FeelWheelIsoService } from './feel-wheel-iso.service';
import { FeelWheelIsoComponent } from './feel-wheel-iso.component';
import { FeelWheelIsoDetailComponent } from './feel-wheel-iso-detail.component';
import { FeelWheelIsoUpdateComponent } from './feel-wheel-iso-update.component';
import { FeelWheelIsoDeletePopupComponent } from './feel-wheel-iso-delete-dialog.component';
import { IFeelWheelIso } from 'app/shared/model/efwservice/feel-wheel-iso.model';

@Injectable({ providedIn: 'root' })
export class FeelWheelIsoResolve implements Resolve<IFeelWheelIso> {
    constructor(private service: FeelWheelIsoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFeelWheelIso> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<FeelWheelIso>) => response.ok),
                map((feelWheel: HttpResponse<FeelWheelIso>) => feelWheel.body)
            );
        }
        return of(new FeelWheelIso());
    }
}

export const feelWheelRoute: Routes = [
    {
        path: '',
        component: FeelWheelIsoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceFeelWheel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: FeelWheelIsoDetailComponent,
        resolve: {
            feelWheel: FeelWheelIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceFeelWheel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: FeelWheelIsoUpdateComponent,
        resolve: {
            feelWheel: FeelWheelIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceFeelWheel.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: FeelWheelIsoUpdateComponent,
        resolve: {
            feelWheel: FeelWheelIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceFeelWheel.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const feelWheelPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: FeelWheelIsoDeletePopupComponent,
        resolve: {
            feelWheel: FeelWheelIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceFeelWheel.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
