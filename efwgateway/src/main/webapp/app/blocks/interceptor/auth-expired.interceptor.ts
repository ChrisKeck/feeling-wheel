import {HttpErrorResponse, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {LoginService} from 'app/core/login/login.service';
import {Observable} from 'rxjs';
import {tap} from 'rxjs/operators';

@Injectable()
export class AuthExpiredInterceptor implements HttpInterceptor {
    constructor(private loginService: LoginService) {
    }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(tap((event: HttpEvent<any>) => {
        }, (err: any) => {
            if (err instanceof HttpErrorResponse) {
                if (err.status === 401) {
                    this.loginService.logout();
                }
            }
        }));
    }
}
