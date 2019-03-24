import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFeelWheelIso } from 'app/shared/model/efwservice/feel-wheel-iso.model';

@Component({
    selector: 'jhi-feel-wheel-iso-detail',
    templateUrl: './feel-wheel-iso-detail.component.html'
})
export class FeelWheelIsoDetailComponent implements OnInit {
    feelWheel: IFeelWheelIso;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ feelWheel }) => {
            this.feelWheel = feelWheel;
        });
    }

    previousState() {
        window.history.back();
    }
}
