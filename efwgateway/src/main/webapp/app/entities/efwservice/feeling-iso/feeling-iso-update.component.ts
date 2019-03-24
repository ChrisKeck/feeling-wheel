import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IFeelingIso } from 'app/shared/model/efwservice/feeling-iso.model';
import { FeelingIsoService } from './feeling-iso.service';
import { IFeelWheelIso } from 'app/shared/model/efwservice/feel-wheel-iso.model';
import { FeelWheelIsoService } from 'app/entities/efwservice/feel-wheel-iso';

@Component({
    selector: 'jhi-feeling-iso-update',
    templateUrl: './feeling-iso-update.component.html'
})
export class FeelingIsoUpdateComponent implements OnInit {
    feeling: IFeelingIso;
    isSaving: boolean;

    feelwheels: IFeelWheelIso[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected feelingService: FeelingIsoService,
        protected feelWheelService: FeelWheelIsoService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ feeling }) => {
            this.feeling = feeling;
        });
        this.feelWheelService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IFeelWheelIso[]>) => mayBeOk.ok),
                map((response: HttpResponse<IFeelWheelIso[]>) => response.body)
            )
            .subscribe((res: IFeelWheelIso[]) => (this.feelwheels = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.feeling.id !== undefined) {
            this.subscribeToSaveResponse(this.feelingService.update(this.feeling));
        } else {
            this.subscribeToSaveResponse(this.feelingService.create(this.feeling));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeelingIso>>) {
        result.subscribe((res: HttpResponse<IFeelingIso>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackFeelWheelById(index: number, item: IFeelWheelIso) {
        return item.id;
    }
}
