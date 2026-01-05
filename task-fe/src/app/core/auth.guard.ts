import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { filter, take, map, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate, CanActivateChild {
  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): Observable<boolean> {
    return this.checkAuth();
  }

  canActivateChild(): Observable<boolean> {
    return this.checkAuth();
  }

  private checkAuth(): Observable<boolean> {
    return this.authService.authState().pipe(
      filter((state) => state !== 'checking'),
      take(1),
      map((stateAuth) => {
        if (stateAuth === 'authenticated') {
          return true;
        }
        this.router.navigate(['/home/login']);
        return false;
      })
    );
  }
}
