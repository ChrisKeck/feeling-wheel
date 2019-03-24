/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EfwgatewayTestModule } from '../../../../test.module';
import { FeelWheelIsoDetailComponent } from 'app/entities/efwservice/feel-wheel-iso/feel-wheel-iso-detail.component';
import { FeelWheelIso } from 'app/shared/model/efwservice/feel-wheel-iso.model';

describe('Component Tests', () => {
    describe('FeelWheelIso Management Detail Component', () => {
        let comp: FeelWheelIsoDetailComponent;
        let fixture: ComponentFixture<FeelWheelIsoDetailComponent>;
        const route = ({ data: of({ feelWheel: new FeelWheelIso(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EfwgatewayTestModule],
                declarations: [FeelWheelIsoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FeelWheelIsoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FeelWheelIsoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.feelWheel).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
