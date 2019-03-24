import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { EmployeeIso } from 'app/shared/model/efwservice/employee-iso.model';
import { EmployeeIsoService } from './employee-iso.service';
import { EmployeeIsoComponent } from './employee-iso.component';
import { EmployeeIsoDetailComponent } from './employee-iso-detail.component';
import { EmployeeIsoUpdateComponent } from './employee-iso-update.component';
import { EmployeeIsoDeletePopupComponent } from './employee-iso-delete-dialog.component';
import { IEmployeeIso } from 'app/shared/model/efwservice/employee-iso.model';

@Injectable({ providedIn: 'root' })
export class EmployeeIsoResolve implements Resolve<IEmployeeIso> {
    constructor(private service: EmployeeIsoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IEmployeeIso> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<EmployeeIso>) => response.ok),
                map((employee: HttpResponse<EmployeeIso>) => employee.body)
            );
        }
        return of(new EmployeeIso());
    }
}

export const employeeRoute: Routes = [
    {
        path: '',
        component: EmployeeIsoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceEmployee.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: EmployeeIsoDetailComponent,
        resolve: {
            employee: EmployeeIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceEmployee.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: EmployeeIsoUpdateComponent,
        resolve: {
            employee: EmployeeIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceEmployee.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: EmployeeIsoUpdateComponent,
        resolve: {
            employee: EmployeeIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceEmployee.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const employeePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: EmployeeIsoDeletePopupComponent,
        resolve: {
            employee: EmployeeIsoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'efwgatewayApp.efwserviceEmployee.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
