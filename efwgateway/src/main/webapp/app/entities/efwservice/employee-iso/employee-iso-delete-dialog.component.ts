import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {NgbActiveModal, NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';

import {IEmployeeIso} from 'app/shared/model/efwservice/employee-iso.model';
import {JhiEventManager} from 'ng-jhipster';
import {EmployeeIsoService} from './employee-iso.service';

@Component({
               selector: 'jhi-employee-iso-delete-dialog', templateUrl: './employee-iso-delete-dialog.component.html'
           })
export class EmployeeIsoDeleteDialogComponent {
    employee: IEmployeeIso;

    constructor(protected employeeService: EmployeeIsoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.employeeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                                            name: 'employeeListModification', content: 'Deleted an employee'
                                        });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
               selector: 'jhi-employee-iso-delete-popup', template: ''
           })
export class EmployeeIsoDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({employee}) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(EmployeeIsoDeleteDialogComponent as Component, {
                    size: 'lg', backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.employee = employee;
                this.ngbModalRef.result.then(result => {
                    this.router.navigate(['/employee-iso', {outlets: {popup: null}}]);
                    this.ngbModalRef = null;
                }, reason => {
                    this.router.navigate(['/employee-iso', {outlets: {popup: null}}]);
                    this.ngbModalRef = null;
                });
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
