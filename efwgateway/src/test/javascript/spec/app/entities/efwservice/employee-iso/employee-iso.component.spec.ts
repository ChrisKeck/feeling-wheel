/* tslint:disable max-line-length */
import {HttpHeaders, HttpResponse} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ActivatedRoute, Data} from '@angular/router';
import {EmployeeIsoComponent} from 'app/entities/efwservice/employee-iso/employee-iso.component';
import {EmployeeIsoService} from 'app/entities/efwservice/employee-iso/employee-iso.service';
import {EmployeeIso} from 'app/shared/model/efwservice/employee-iso.model';
import {of} from 'rxjs';

import {EfwgatewayTestModule} from '../../../../test.module';

describe('Component Tests', () => {
    describe('EmployeeIso Management Component', () => {
        let comp: EmployeeIsoComponent;
        let fixture: ComponentFixture<EmployeeIsoComponent>;
        let service: EmployeeIsoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                                               imports: [EfwgatewayTestModule], declarations: [EmployeeIsoComponent], providers: [{
                           provide: ActivatedRoute, useValue: {
                               data: {
                                   subscribe: (fn: (value: Data) => void) => fn({
                                                                                    pagingParams: {
                                                                                        predicate: 'id', reverse: false, page: 0
                                                                                    }
                                                                                })
                               }
                           }
                       }]
                                           })
                   .overrideTemplate(EmployeeIsoComponent, '')
                   .compileComponents();

            fixture = TestBed.createComponent(EmployeeIsoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(EmployeeIsoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(of(new HttpResponse({
                                                                            body: [new EmployeeIso(123)], headers
                                                                        })));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.employees[0]).toEqual(jasmine.objectContaining({id: 123}));
        });

        it('should load a page', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(of(new HttpResponse({
                                                                            body: [new EmployeeIso(123)], headers
                                                                        })));

            // WHEN
            comp.loadPage(1);

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.employees[0]).toEqual(jasmine.objectContaining({id: 123}));
        });

        it('should re-initialize the page', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(of(new HttpResponse({
                                                                            body: [new EmployeeIso(123)], headers
                                                                        })));

            // WHEN
            comp.loadPage(1);
            comp.reset();

            // THEN
            expect(comp.page).toEqual(0);
            expect(service.query).toHaveBeenCalledTimes(2);
            expect(comp.employees[0]).toEqual(jasmine.objectContaining({id: 123}));
        });
        it('should calculate the sort attribute for an id', () => {
            // WHEN
            const result = comp.sort();

            // THEN
            expect(result).toEqual(['id,asc']);
        });

        it('should calculate the sort attribute for a non-id attribute', () => {
            // GIVEN
            comp.predicate = 'name';

            // WHEN
            const result = comp.sort();

            // THEN
            expect(result).toEqual(['name,asc', 'id']);
        });
    });
});
