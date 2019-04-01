/* tslint:disable max-line-length */
import {ComponentFixture, fakeAsync, inject, TestBed, tick} from '@angular/core/testing';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FeelingIsoDeleteDialogComponent} from 'app/entities/efwservice/feeling-iso/feeling-iso-delete-dialog.component';
import {FeelingIsoService} from 'app/entities/efwservice/feeling-iso/feeling-iso.service';
import {JhiEventManager} from 'ng-jhipster';
import {of} from 'rxjs';

import {EfwgatewayTestModule} from '../../../../test.module';

describe('Component Tests', () => {
    describe('FeelingIso Management Delete Component', () => {
        let comp: FeelingIsoDeleteDialogComponent;
        let fixture: ComponentFixture<FeelingIsoDeleteDialogComponent>;
        let service: FeelingIsoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                                               imports: [EfwgatewayTestModule], declarations: [FeelingIsoDeleteDialogComponent]
                                           })
                   .overrideTemplate(FeelingIsoDeleteDialogComponent, '')
                   .compileComponents();
            fixture = TestBed.createComponent(FeelingIsoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FeelingIsoService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject([], fakeAsync(() => {
                // GIVEN
                spyOn(service, 'delete').and.returnValue(of({}));

                // WHEN
                comp.confirmDelete(123);
                tick();

                // THEN
                expect(service.delete).toHaveBeenCalledWith(123);
                expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
            })));
        });
    });
});
