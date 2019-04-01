/* tslint:disable max-line-length */
import {ComponentFixture, fakeAsync, inject, TestBed, tick} from '@angular/core/testing';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {FeelWheelIsoDeleteDialogComponent} from 'app/entities/efwservice/feel-wheel-iso/feel-wheel-iso-delete-dialog.component';
import {FeelWheelIsoService} from 'app/entities/efwservice/feel-wheel-iso/feel-wheel-iso.service';
import {JhiEventManager} from 'ng-jhipster';
import {of} from 'rxjs';

import {EfwgatewayTestModule} from '../../../../test.module';

describe('Component Tests', () => {
    describe('FeelWheelIso Management Delete Component', () => {
        let comp: FeelWheelIsoDeleteDialogComponent;
        let fixture: ComponentFixture<FeelWheelIsoDeleteDialogComponent>;
        let service: FeelWheelIsoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                                               imports: [EfwgatewayTestModule], declarations: [FeelWheelIsoDeleteDialogComponent]
                                           })
                   .overrideTemplate(FeelWheelIsoDeleteDialogComponent, '')
                   .compileComponents();
            fixture = TestBed.createComponent(FeelWheelIsoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FeelWheelIsoService);
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
