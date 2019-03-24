import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FeelingIso } from 'app/shared/model/efwservice/feeling-iso.model';
import { FeelingIsoService } from './feeling-iso.service';
import { FeelingIsoComponent } from './feeling-iso.component';
import { FeelingIsoDetailComponent } from './feeling-iso-detail.component';
import { FeelingIsoUpdateComponent } from './feeling-iso-update.component';
import { FeelingIsoDeletePopupComponent } from './feeling-iso-delete-dialog.component';
import { IFeelingIso } from 'app/shared/model/efwservice/feeling-iso.model';

@Injectable({ providedIn: 'root' })
export class FeelingIsoResolve implements Resolve<IFeelingIso> {
    constructor(private service: FeelingIsoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFeelingIso> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<FeelingIso>) => response.ok),
                map((feeling: HttpResponse<FeelingIso>) => feeling.body)
            );
        }
        return of(new FeelingIso());
    }
}

export const feelingRoute: Routes = [
    {
        path: '',
        component: FeelingIsoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceFeeling.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: FeelingIsoDetailComponent,
        resolve: {
            feeling: FeelingIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceFeeling.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: FeelingIsoUpdateComponent,
        resolve: {
            feeling: FeelingIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceFeeling.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: FeelingIsoUpdateComponent,
        resolve: {
            feeling: FeelingIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceFeeling.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const feelingPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: FeelingIsoDeletePopupComponent,
        resolve: {
            feeling: FeelingIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceFeeling.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
