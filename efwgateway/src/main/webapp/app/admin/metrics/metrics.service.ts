import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';

import {SERVER_API_URL} from 'app/app.constants';
import {Observable} from 'rxjs';

@Injectable({providedIn: 'root'})
export class JhiMetricsService {
    constructor(private http: HttpClient) {
    }

    getMetrics(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'management/jhi-metrics');
    }

    threadDump(): Observable<any> {
        return this.http.get(SERVER_API_URL + 'management/threaddump');
    }
}
