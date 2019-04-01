import {HttpClient, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IFeelWheelIso} from 'app/shared/model/efwservice/feel-wheel-iso.model';
import * as moment from 'moment';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

type EntityResponseType = HttpResponse<IFeelWheelIso>;
type EntityArrayResponseType = HttpResponse<IFeelWheelIso[]>;

@Injectable({providedIn: 'root'})
export class FeelWheelIsoService {
    public resourceUrl = SERVER_API_URL + 'efwservice/api/feel-wheels';
    public resourceSearchUrl = SERVER_API_URL + 'efwservice/api/_search/feel-wheels';

    constructor(protected http: HttpClient) {
    }

    create(feelWheel: IFeelWheelIso): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(feelWheel);
        return this.http
                   .post<IFeelWheelIso>(this.resourceUrl, copy, {observe: 'response'})
                   .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(feelWheel: IFeelWheelIso): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(feelWheel);
        return this.http
                   .put<IFeelWheelIso>(this.resourceUrl, copy, {observe: 'response'})
                   .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
                   .get<IFeelWheelIso>(`${this.resourceUrl}/${id}`, {observe: 'response'})
                   .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
                   .get<IFeelWheelIso[]>(this.resourceUrl, {params: options, observe: 'response'})
                   .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
                   .get<IFeelWheelIso[]>(this.resourceSearchUrl, {params: options, observe: 'response'})
                   .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    protected convertDateFromClient(feelWheel: IFeelWheelIso): IFeelWheelIso {
        const copy: IFeelWheelIso = Object.assign({}, feelWheel, {
            from: feelWheel.from != null && feelWheel.from.isValid() ? feelWheel.from.toJSON() : null,
            to: feelWheel.to != null && feelWheel.to.isValid() ? feelWheel.to.toJSON() : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.from = res.body.from != null ? moment(res.body.from) : null;
            res.body.to = res.body.to != null ? moment(res.body.to) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((feelWheel: IFeelWheelIso) => {
                feelWheel.from = feelWheel.from != null ? moment(feelWheel.from) : null;
                feelWheel.to = feelWheel.to != null ? moment(feelWheel.to) : null;
            });
        }
        return res;
    }
}
