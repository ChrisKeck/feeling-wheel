import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IFeelingIso } from 'app/shared/model/efwservice/feeling-iso.model';

type EntityResponseType = HttpResponse<IFeelingIso>;
type EntityArrayResponseType = HttpResponse<IFeelingIso[]>;

@Injectable({ providedIn: 'root' })
export class FeelingIsoService {
    public resourceUrl = SERVER_API_URL + 'efwservice/api/feelings';
    public resourceSearchUrl = SERVER_API_URL + 'efwservice/api/_search/feelings';

    constructor(protected http: HttpClient) {}

    create(feeling: IFeelingIso): Observable<EntityResponseType> {
        return this.http.post<IFeelingIso>(this.resourceUrl, feeling, { observe: 'response' });
    }

    update(feeling: IFeelingIso): Observable<EntityResponseType> {
        return this.http.put<IFeelingIso>(this.resourceUrl, feeling, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IFeelingIso>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFeelingIso[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IFeelingIso[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
    }
}
