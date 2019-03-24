import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFeelingIso } from 'app/shared/model/efwservice/feeling-iso.model';
import { FeelingIsoService } from './feeling-iso.service';

@Component({
    selector: 'jhi-feeling-iso-delete-dialog',
    templateUrl: './feeling-iso-delete-dialog.component.html'
})
export class FeelingIsoDeleteDialogComponent {
    feeling: IFeelingIso;

    constructor(protected feelingService: FeelingIsoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.feelingService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'feelingListModification',
                content: 'Deleted an feeling'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-feeling-iso-delete-popup',
    template: ''
})
export class FeelingIsoDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ feeling }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FeelingIsoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.feeling = feeling;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/feeling-iso', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/feeling-iso', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
