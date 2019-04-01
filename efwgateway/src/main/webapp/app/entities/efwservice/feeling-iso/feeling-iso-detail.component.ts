import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';

import {IFeelingIso} from 'app/shared/model/efwservice/feeling-iso.model';

@Component({
               selector: 'jhi-feeling-iso-detail', templateUrl: './feeling-iso-detail.component.html'
           })
export class FeelingIsoDetailComponent implements OnInit {
    feeling: IFeelingIso;

    constructor(protected activatedRoute: ActivatedRoute) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({feeling}) => {
            this.feeling = feeling;
        });
    }

    previousState() {
        window.history.back();
    }
}
