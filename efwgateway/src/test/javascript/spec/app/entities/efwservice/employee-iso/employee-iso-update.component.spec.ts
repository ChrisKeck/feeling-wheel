/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { EfwgatewayTestModule } from '../../../../test.module';
import { EmployeeIsoUpdateComponent } from 'app/entities/efwservice/employee-iso/employee-iso-update.component';
import { EmployeeIsoService } from 'app/entities/efwservice/employee-iso/employee-iso.service';
import { EmployeeIso } from 'app/shared/model/efwservice/employee-iso.model';

describe('Component Tests', () => {
    describe('EmployeeIso Management Update Component', () => {
        let comp: EmployeeIsoUpdateComponent;
        let fixture: ComponentFixture<EmployeeIsoUpdateComponent>;
        let service: EmployeeIsoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfwgatewayTestModule],
                declarations: [EmployeeIsoUpdateComponent]
            })
                .overrideTemplate(EmployeeIsoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(EmployeeIsoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmployeeIsoService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new EmployeeIso(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.employee = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new EmployeeIso();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.employee = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
