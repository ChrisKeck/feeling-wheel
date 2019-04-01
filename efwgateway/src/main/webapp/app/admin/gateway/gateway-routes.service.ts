import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';

import {SERVER_API_URL} from 'app/app.constants';
import {Observable} from 'rxjs';
import {GatewayRoute} from './gateway-route.model';

@Injectable()
export class GatewayRoutesService {
    constructor(private http: HttpClient) {
    }

    findAll(): Observable<GatewayRoute[]> {
        return this.http.get<GatewayRoute[]>(SERVER_API_URL + 'api/gateway/routes/');
    }
}
