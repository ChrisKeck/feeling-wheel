import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

import {NgbActiveModal, NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';

import {IFeelWheelIso} from 'app/shared/model/efwservice/feel-wheel-iso.model';
import {JhiEventManager} from 'ng-jhipster';
import {FeelWheelIsoService} from './feel-wheel-iso.service';

@Component({
               selector: 'jhi-feel-wheel-iso-delete-dialog', templateUrl: './feel-wheel-iso-delete-dialog.component.html'
           })
export class FeelWheelIsoDeleteDialogComponent {
    feelWheel: IFeelWheelIso;

    constructor(protected feelWheelService: FeelWheelIsoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.feelWheelService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                                            name: 'feelWheelListModification', content: 'Deleted an feelWheel'
                                        });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
               selector: 'jhi-feel-wheel-iso-delete-popup', template: ''
           })
export class FeelWheelIsoDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({feelWheel}) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FeelWheelIsoDeleteDialogComponent as Component, {
                    size: 'lg', backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.feelWheel = feelWheel;
                this.ngbModalRef.result.then(result => {
                    this.router.navigate(['/feel-wheel-iso', {outlets: {popup: null}}]);
                    this.ngbModalRef = null;
                }, reason => {
                    this.router.navigate(['/feel-wheel-iso', {outlets: {popup: null}}]);
                    this.ngbModalRef = null;
                });
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
