/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EfwgatewayTestModule } from '../../../../test.module';
import { FeelingIsoDetailComponent } from 'app/entities/efwservice/feeling-iso/feeling-iso-detail.component';
import { FeelingIso } from 'app/shared/model/efwservice/feeling-iso.model';

describe('Component Tests', () => {
    describe('FeelingIso Management Detail Component', () => {
        let comp: FeelingIsoDetailComponent;
        let fixture: ComponentFixture<FeelingIsoDetailComponent>;
        const route = ({ data: of({ feeling: new FeelingIso(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfwgatewayTestModule],
                declarations: [FeelingIsoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FeelingIsoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FeelingIsoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.feeling).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
