import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IFeelWheelIso } from 'app/shared/model/efwservice/feel-wheel-iso.model';
import { FeelWheelIsoService } from './feel-wheel-iso.service';
import { IEmployeeIso } from 'app/shared/model/efwservice/employee-iso.model';
import { EmployeeIsoService } from 'app/entities/efwservice/employee-iso';

@Component({
    selector: 'jhi-feel-wheel-iso-update',
    templateUrl: './feel-wheel-iso-update.component.html'
})
export class FeelWheelIsoUpdateComponent implements OnInit {
    feelWheel: IFeelWheelIso;
    isSaving: boolean;

    employees: IEmployeeIso[];
    from: string;
    to: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected feelWheelService: FeelWheelIsoService,
        protected employeeService: EmployeeIsoService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ feelWheel }) => {
            this.feelWheel = feelWheel;
            this.from = this.feelWheel.from != null ? this.feelWheel.from.format(DATE_TIME_FORMAT) : null;
            this.to = this.feelWheel.to != null ? this.feelWheel.to.format(DATE_TIME_FORMAT) : null;
        });
        this.employeeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IEmployeeIso[]>) => mayBeOk.ok),
                map((response: HttpResponse<IEmployeeIso[]>) => response.body)
            )
            .subscribe((res: IEmployeeIso[]) => (this.employees = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.feelWheel.from = this.from != null ? moment(this.from, DATE_TIME_FORMAT) : null;
        this.feelWheel.to = this.to != null ? moment(this.to, DATE_TIME_FORMAT) : null;
        if (this.feelWheel.id !== undefined) {
            this.subscribeToSaveResponse(this.feelWheelService.update(this.feelWheel));
        } else {
            this.subscribeToSaveResponse(this.feelWheelService.create(this.feelWheel));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeelWheelIso>>) {
        result.subscribe((res: HttpResponse<IFeelWheelIso>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackEmployeeById(index: number, item: IEmployeeIso) {
        return item.id;
    }
}
