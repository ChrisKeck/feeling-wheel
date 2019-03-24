/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EfwgatewayTestModule } from '../../../../test.module';
import { EmployeeIsoDetailComponent } from 'app/entities/efwservice/employee-iso/employee-iso-detail.component';
import { EmployeeIso } from 'app/shared/model/efwservice/employee-iso.model';

describe('Component Tests', () => {
    describe('EmployeeIso Management Detail Component', () => {
        let comp: EmployeeIsoDetailComponent;
        let fixture: ComponentFixture<EmployeeIsoDetailComponent>;
        const route = ({ data: of({ employee: new EmployeeIso(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfwgatewayTestModule],
                declarations: [EmployeeIsoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(EmployeeIsoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(EmployeeIsoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.employee).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
