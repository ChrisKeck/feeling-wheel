import {HttpClient, HttpResponse} from '@angular/common/http';
import {Injectable} from '@angular/core';

import {SERVER_API_URL} from 'app/app.constants';
import {createRequestOption} from 'app/shared';
import {IEmployeeIso} from 'app/shared/model/efwservice/employee-iso.model';
import {Observable} from 'rxjs';

type EntityResponseType = HttpResponse<IEmployeeIso>;
type EntityArrayResponseType = HttpResponse<IEmployeeIso[]>;

@Injectable({providedIn: 'root'})
export class EmployeeIsoService {
    public resourceUrl = SERVER_API_URL + 'efwservice/api/employees';
    public resourceSearchUrl = SERVER_API_URL + 'efwservice/api/_search/employees';

    constructor(protected http: HttpClient) {
    }

    create(employee: IEmployeeIso): Observable<EntityResponseType> {
        return this.http.post<IEmployeeIso>(this.resourceUrl, employee, {observe: 'response'});
    }

    update(employee: IEmployeeIso): Observable<EntityResponseType> {
        return this.http.put<IEmployeeIso>(this.resourceUrl, employee, {observe: 'response'});
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IEmployeeIso>(`${this.resourceUrl}/${id}`, {observe: 'response'});
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEmployeeIso[]>(this.resourceUrl, {params: options, observe: 'response'});
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, {observe: 'response'});
    }

    search(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IEmployeeIso[]>(this.resourceSearchUrl, {params: options, observe: 'response'});
    }
}
