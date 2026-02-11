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
import { Observable, catchError, throwError } from 'rxjs';
import { AuthService, SKIP_AUTH_ERROR } from '../services/auth.service';
import { MessageService } from 'primeng/api';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
  constructor(
    private toastr: ToastrService,
    private authService: AuthService,
    private router: Router,
    private messageService:MessageService
  ) {}
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    return next.handle(request).pipe(
      catchError((err: any) => {
        if (err instanceof HttpErrorResponse) {
          if (!request.context.get(SKIP_AUTH_ERROR)) {
            this.messageService.add({
            severity: 'error',
            summary: 'Attention',
            detail: err?.error?.message || err?.error?.messages[0],
            life: 3000,
          });
          }
        }
        return throwError(() => err);
      })
    );
  }
}
