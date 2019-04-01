/* tslint:disable max-line-length */
import {HttpHeaders, HttpResponse} from '@angular/common/http';
import {ComponentFixture, TestBed} from '@angular/core/testing';
import {ActivatedRoute, Data} from '@angular/router';
import {FeelWheelIsoComponent} from 'app/entities/efwservice/feel-wheel-iso/feel-wheel-iso.component';
import {FeelWheelIsoService} from 'app/entities/efwservice/feel-wheel-iso/feel-wheel-iso.service';
import {FeelWheelIso} from 'app/shared/model/efwservice/feel-wheel-iso.model';
import {of} from 'rxjs';

import {EfwgatewayTestModule} from '../../../../test.module';

describe('Component Tests', () => {
    describe('FeelWheelIso Management Component', () => {
        let comp: FeelWheelIsoComponent;
        let fixture: ComponentFixture<FeelWheelIsoComponent>;
        let service: FeelWheelIsoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                                               imports: [EfwgatewayTestModule], declarations: [FeelWheelIsoComponent], providers: [{
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
                   .overrideTemplate(FeelWheelIsoComponent, '')
                   .compileComponents();

            fixture = TestBed.createComponent(FeelWheelIsoComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FeelWheelIsoService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(of(new HttpResponse({
                                                                            body: [new FeelWheelIso(123)], headers
                                                                        })));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.feelWheels[0]).toEqual(jasmine.objectContaining({id: 123}));
        });

        it('should load a page', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(of(new HttpResponse({
                                                                            body: [new FeelWheelIso(123)], headers
                                                                        })));

            // WHEN
            comp.loadPage(1);

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.feelWheels[0]).toEqual(jasmine.objectContaining({id: 123}));
        });

        it('should re-initialize the page', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(of(new HttpResponse({
                                                                            body: [new FeelWheelIso(123)], headers
                                                                        })));

            // WHEN
            comp.loadPage(1);
            comp.reset();

            // THEN
            expect(comp.page).toEqual(0);
            expect(service.query).toHaveBeenCalledTimes(2);
            expect(comp.feelWheels[0]).toEqual(jasmine.objectContaining({id: 123}));
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
