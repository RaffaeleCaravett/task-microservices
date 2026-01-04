import {
  HttpInterceptor,
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpErrorResponse,
} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Observable, catchError } from 'rxjs';
import { AuthService } from '../services/auth.service';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private toastr: ToastrService,
    private authService: AuthService,
    private router: Router
  ) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((err: any) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status != 401 && err.status != 403) {
            this.toastr.error(err?.error?.message || err?.error?.messages[0]);
          } else {
            if (this.authService.getRefreshToken()) {
            } else {
              this.toastr.error(err?.error?.message || err?.error?.messages[0]);
              this.authService.setIsLoggedIn(false);
              localStorage.clear();
              this.authService.setUser(null);
              this.authService.setCompany(null);
              this.authService.setAccessToken(null);
              this.authService.setRefreshToken(null);
            }
          }
        }
        return new Observable<HttpEvent<any>>();
      })
    );
  }
}
