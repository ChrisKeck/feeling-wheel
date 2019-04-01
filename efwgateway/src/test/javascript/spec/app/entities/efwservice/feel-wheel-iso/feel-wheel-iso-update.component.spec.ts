/* tslint:disable max-line-length */
import {HttpResponse} from '@angular/common/http';
import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {FeelWheelIsoUpdateComponent} from 'app/entities/efwservice/feel-wheel-iso/feel-wheel-iso-update.component';
import {FeelWheelIsoService} from 'app/entities/efwservice/feel-wheel-iso/feel-wheel-iso.service';
import {FeelWheelIso} from 'app/shared/model/efwservice/feel-wheel-iso.model';
import {of} from 'rxjs';

import {EfwgatewayTestModule} from '../../../../test.module';

describe('Component Tests', () => {
    describe('FeelWheelIso Management Update Component', () => {
        let comp: FeelWheelIsoUpdateComponent;
        let fixture: ComponentFixture<FeelWheelIsoUpdateComponent>;
        let service: FeelWheelIsoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                                               imports: [EfwgatewayTestModule], declarations: [FeelWheelIsoUpdateComponent]
                                           })
                   .overrideTemplate(FeelWheelIsoUpdateComponent, '')
                   .compileComponents();

            fixture = TestBed.createComponent(FeelWheelIsoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FeelWheelIsoService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new FeelWheelIso(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({body: entity})));
                comp.feelWheel = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new FeelWheelIso();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({body: entity})));
                comp.feelWheel = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
