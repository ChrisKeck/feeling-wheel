import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeeIso } from 'app/shared/model/efwservice/employee-iso.model';

@Component({
    selector: 'jhi-employee-iso-detail',
    templateUrl: './employee-iso-detail.component.html'
})
export class EmployeeIsoDetailComponent implements OnInit {
    employee: IEmployeeIso;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ employee }) => {
            this.employee = employee;
        });
    }

    previousState() {
        window.history.back();
    }
}
