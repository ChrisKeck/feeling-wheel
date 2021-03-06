import {HttpErrorResponse, HttpResponse} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {IEmployeeIso} from 'app/shared/model/efwservice/employee-iso.model';
import {JhiAlertService} from 'ng-jhipster';
import {Observable} from 'rxjs';
import {filter, map} from 'rxjs/operators';
import {EmployeeIsoService} from './employee-iso.service';

@Component({
               selector: 'jhi-employee-iso-update', templateUrl: './employee-iso-update.component.html'
           })
export class EmployeeIsoUpdateComponent implements OnInit {
    employee: IEmployeeIso;
    isSaving: boolean;

    employees: IEmployeeIso[];

    constructor(protected jhiAlertService: JhiAlertService, protected employeeService: EmployeeIsoService, protected activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({employee}) => {
            this.employee = employee;
        });
        this.employeeService
            .query()
            .pipe(filter((mayBeOk: HttpResponse<IEmployeeIso[]>) => mayBeOk.ok), map((response: HttpResponse<IEmployeeIso[]>) => response.body))
            .subscribe((res: IEmployeeIso[]) => (this.employees = res
                .filter(tpl => tpl.id !== this.employee.id && tpl.employeeId !== this.employee.id)),
                       (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.employee.id !== undefined) {
            this.subscribeToSaveResponse(this.employeeService.update(this.employee));
        } else {
            this.subscribeToSaveResponse(this.employeeService.create(this.employee));
        }
    }

    trackEmployeeById(index: number, item: IEmployeeIso) {
        return item.id;
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IEmployeeIso>>) {
        result.subscribe((res: HttpResponse<IEmployeeIso>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
