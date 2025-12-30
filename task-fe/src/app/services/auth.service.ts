import { HttpClient } from '@angular/common/http';
import { Token } from '@angular/compiler';
import { inject, Injectable } from '@angular/core';
import { Company, CompanySignup, UserLogin } from '../interfaces/interfaces';
import { API_URL } from '../core/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private isLoggedIn: boolean = false;
  private user: any = null;
  private http: HttpClient = inject(HttpClient);
  public getIsLoggedIn(): boolean {
    return this.isLoggedIn;
  }
  public getUser(): any {
    return this.user;
  }
  public isAdmin(): boolean {
    return this.user?.role == 'ADMIN';
  }
  public isUser(): boolean {
    return this.user?.role == 'USER';
  }
  public isCompany(): boolean {
    return this.user?.role == 'COMPANY';
  }
  public isOwner(): boolean {
    return this.user?.role == 'OWNER';
  }

  public login(body: UserLogin): Observable<Token> {
    return this.http.post<Token>(API_URL.auth + '/login', body);
  }
  public signup(body: CompanySignup): Observable<Company> {
    return this.http.post<Company>(API_URL.auth + 'company/signup', body);
  }
}
