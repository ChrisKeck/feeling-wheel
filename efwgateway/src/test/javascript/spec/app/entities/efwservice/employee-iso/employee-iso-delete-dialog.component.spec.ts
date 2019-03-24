/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { EfwgatewayTestModule } from '../../../../test.module';
import { EmployeeIsoDeleteDialogComponent } from 'app/entities/efwservice/employee-iso/employee-iso-delete-dialog.component';
import { EmployeeIsoService } from 'app/entities/efwservice/employee-iso/employee-iso.service';

describe('Component Tests', () => {
    describe('EmployeeIso Management Delete Component', () => {
        let comp: EmployeeIsoDeleteDialogComponent;
        let fixture: ComponentFixture<EmployeeIsoDeleteDialogComponent>;
        let service: EmployeeIsoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfwgatewayTestModule],
                declarations: [EmployeeIsoDeleteDialogComponent]
            })
                .overrideTemplate(EmployeeIsoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(EmployeeIsoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmployeeIsoService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
