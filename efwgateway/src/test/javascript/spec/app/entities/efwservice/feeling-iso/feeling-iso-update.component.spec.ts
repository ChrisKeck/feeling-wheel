/* tslint:disable max-line-length */
import {HttpResponse} from '@angular/common/http';
import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {FeelingIsoUpdateComponent} from 'app/entities/efwservice/feeling-iso/feeling-iso-update.component';
import {FeelingIsoService} from 'app/entities/efwservice/feeling-iso/feeling-iso.service';
import {FeelingIso} from 'app/shared/model/efwservice/feeling-iso.model';
import {of} from 'rxjs';

import {EfwgatewayTestModule} from '../../../../test.module';

describe('Component Tests', () => {
    describe('FeelingIso Management Update Component', () => {
        let comp: FeelingIsoUpdateComponent;
        let fixture: ComponentFixture<FeelingIsoUpdateComponent>;
        let service: FeelingIsoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                                               imports: [EfwgatewayTestModule], declarations: [FeelingIsoUpdateComponent]
                                           })
                   .overrideTemplate(FeelingIsoUpdateComponent, '')
                   .compileComponents();

            fixture = TestBed.createComponent(FeelingIsoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FeelingIsoService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new FeelingIso(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({body: entity})));
                comp.feeling = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new FeelingIso();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({body: entity})));
                comp.feeling = entity;
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
