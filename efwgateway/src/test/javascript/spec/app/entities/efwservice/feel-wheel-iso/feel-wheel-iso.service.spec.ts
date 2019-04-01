/* tslint:disable max-line-length */
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {getTestBed, TestBed} from '@angular/core/testing';
import {FeelWheelIsoService} from 'app/entities/efwservice/feel-wheel-iso/feel-wheel-iso.service';
import {DATE_TIME_FORMAT} from 'app/shared/constants/input.constants';
import {FeelWheelIso, IFeelWheelIso} from 'app/shared/model/efwservice/feel-wheel-iso.model';
import * as moment from 'moment';
import {map, take} from 'rxjs/operators';

describe('Service Tests', () => {
    describe('FeelWheelIso Service', () => {
        let injector: TestBed;
        let service: FeelWheelIsoService;
        let httpMock: HttpTestingController;
        let elemDefault: IFeelWheelIso;
        let currentDate: moment.Moment;
        beforeEach(() => {
            TestBed.configureTestingModule({
                                               imports: [HttpClientTestingModule]
                                           });
            injector = getTestBed();
            service = injector.get(FeelWheelIsoService);
            httpMock = injector.get(HttpTestingController);
            currentDate = moment();

            elemDefault = new FeelWheelIso(0, 'AAAAAAA', currentDate, currentDate);
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign({
                                                              from: currentDate.format(DATE_TIME_FORMAT), to: currentDate.format(DATE_TIME_FORMAT)
                                                          }, elemDefault);
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({body: elemDefault}));

                const req = httpMock.expectOne({method: 'GET'});
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a FeelWheelIso', async () => {
                const returnedFromService = Object.assign({
                                                              id: 0,
                                                              from: currentDate.format(DATE_TIME_FORMAT),
                                                              to: currentDate.format(DATE_TIME_FORMAT)
                                                          }, elemDefault);
                const expected = Object.assign({
                                                   from: currentDate, to: currentDate
                                               }, returnedFromService);
                service
                    .create(new FeelWheelIso(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({body: expected}));
                const req = httpMock.expectOne({method: 'POST'});
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a FeelWheelIso', async () => {
                const returnedFromService = Object.assign({
                                                              subject: 'BBBBBB',
                                                              from: currentDate.format(DATE_TIME_FORMAT),
                                                              to: currentDate.format(DATE_TIME_FORMAT)
                                                          }, elemDefault);

                const expected = Object.assign({
                                                   from: currentDate, to: currentDate
                                               }, returnedFromService);
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({body: expected}));
                const req = httpMock.expectOne({method: 'PUT'});
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should return a list of FeelWheelIso', async () => {
                const returnedFromService = Object.assign({
                                                              subject: 'BBBBBB',
                                                              from: currentDate.format(DATE_TIME_FORMAT),
                                                              to: currentDate.format(DATE_TIME_FORMAT)
                                                          }, elemDefault);
                const expected = Object.assign({
                                                   from: currentDate, to: currentDate
                                               }, returnedFromService);
                service
                    .query(expected)
                    .pipe(take(1), map(resp => resp.body))
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({method: 'GET'});
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a FeelWheelIso', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({method: 'DELETE'});
                req.flush({status: 200});
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
