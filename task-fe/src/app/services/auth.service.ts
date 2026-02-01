import { HttpClient, HttpContext, HttpContextToken } from '@angular/common/http';
import { Token } from '@angular/compiler';
import { inject, Injectable } from '@angular/core';
import {
  cap,
  citta,
  Company,
  CompanyDTOFromSignup,
  CompanySignup,
  dimensioni,
  formaGiuridica,
  loginSuccess,
  nazione,
  piano,
  regione,
  settore,
  token,
  User,
  UserLogin,
} from '../interfaces/interfaces';
import { API_URL } from '../core/environment';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';

export const SKIP_AUTH_ERROR = new HttpContextToken<boolean>(() => false);

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private isLoggedIn: boolean = false;
  private user: User | null = null;
  private company: Company | null = null;
  private accesstoken: string | null = null;
  private refreshtoken: string | null = null;
  private http: HttpClient = inject(HttpClient);
  private authState$ = new BehaviorSubject<'checking' | 'authenticated' | 'unauthenticated'>(
    'checking'
  );
  protected router: Router = inject(Router);
  constructor() {
    setTimeout(() => {
      this.checkToken();
    }, 500);
  }
  public checkToken() {
    const token: string | null = localStorage.getItem('accessToken');
    const refresh: string | null = localStorage.getItem('refreshToken');
    if (!token) {
      this.authState$.next('unauthenticated');
      return;
    }

    this.verifyAccessToken(token).subscribe({
      next: (verifySuccess: loginSuccess) => {
        if (verifySuccess) {
          this.authState$.next('authenticated');
          this.setAccessToken(token);
          this.setRefreshToken(refresh);
          if (verifySuccess.company) {
            this.setCompany(verifySuccess.company);
          } else {
            this.setUser(verifySuccess.user);
          }
          this.setIsLoggedIn(true);
          if (this.router.url == '/home/login' || this.router.url == '/home/signup') {
            this.router.navigate(['dashboard/landing']);
          }
        }
      },
      error: (err: any) => {
        if (!refresh) {
          this.authState$.next('unauthenticated');
          return;
        }
        this.verifyRefreshToken(refresh).subscribe({
          next: (data: loginSuccess) => {
            if (data) {
              this.authState$.next('authenticated');
              this.setAccessToken(data?.token?.accessToken);
              localStorage.setItem('accessToken', data?.token?.accessToken);
              this.setRefreshToken(refresh);
              if (data.company) {
                this.setCompany(data.company);
              } else {
                this.setUser(data.user);
              }
              this.setIsLoggedIn(true);
              if (this.router.url == '/home/login' || this.router.url == '/home/signup') {
                this.router.navigate(['dashboard/landing']);
              }
            }
          },
          error: (err: any) => {
            this.authState$.next('unauthenticated');
            return;
          },
        });
      },
    });
  }

  authState() {
    return this.authState$.asObservable();
  }
  public getIsLoggedIn(): boolean {
    return this.isLoggedIn;
  }
  public setIsLoggedIn(loggedIn: boolean): void {
    this.isLoggedIn = loggedIn;
  }
  public setUser(user: User | null): void {
    this.user = user;
  }
  public getUser(): any {
    return this.user;
  }
  public setCompany(company: Company | null): void {
    this.company = company;
  }
  public getCompany(): any {
    return this.company;
  }
  public setAccessToken(token: string | null) {
    this.accesstoken = token;
  }
  public setRefreshToken(token: string | null) {
    this.refreshtoken = token;
  }
  public getAccessToken(): string | null {
    return this.accesstoken;
  }
  public getRefreshToken(): string | null {
    return this.refreshtoken;
  }
  public isAdmin(): boolean {
    return (this.user && this.user?.role == 'ADMIN') || false;
  }
  public isUser(): boolean {
    return (this.user && this.user?.role == 'USER') || false;
  }
  public isCompany(): boolean {
    return (this.company && this.company?.role == 'COMPANY') || false;
  }
  public isOwner(): boolean {
    return (this.user && this.user?.role == 'OWNER') || false;
  }
  public signup(body: CompanySignup): Observable<CompanyDTOFromSignup> {
    return this.http.post<CompanyDTOFromSignup>(API_URL.company + '/auth/company/signup', body);
  }
  public verifyCode(email: string, code: string, type: string): Observable<loginSuccess> {
    return this.http.get<loginSuccess>(
      API_URL.auth +
        '/auth/validate/code?code=' +
        code +
        '&email=' +
        email +
        '&type=' +
        type.toUpperCase()
    );
  }
  public login(body: UserLogin, type: string): Observable<boolean> {
    if ('user' == type) {
      return this.http.post<boolean>(API_URL.auth + '/auth/login', body);
    } else {
      return this.http.post<boolean>(API_URL.auth + '/auth/company/login', body);
    }
  }
  public getNazioni(): Observable<nazione[]> {
    return this.http.get<nazione[]>(API_URL.company + '/indirizzo/nazioni');
  }
  public getRegioni(nazioneId: number): Observable<regione[]> {
    return this.http.get<regione[]>(API_URL.company + '/indirizzo/regioni/' + nazioneId);
  }
  public getCitta(regioneId: number): Observable<citta[]> {
    return this.http.get<citta[]>(API_URL.company + '/indirizzo/citta/' + regioneId);
  }
  public getCap(cittaId: number): Observable<cap[]> {
    return this.http.get<cap[]>(API_URL.company + '/indirizzo/cap/' + cittaId);
  }
  public getSettori(): Observable<settore[]> {
    return this.http.get<settore[]>(API_URL.company + '/indirizzo/settori');
  }
  public getForme(): Observable<formaGiuridica[]> {
    return this.http.get<formaGiuridica[]>(API_URL.company + '/indirizzo/forme');
  }
  public getDimensioni(): Observable<dimensioni[]> {
    return this.http.get<dimensioni[]>(API_URL.company + '/indirizzo/dimensioni');
  }
  public getPiani(): Observable<piano[]> {
    return this.http.get<piano[]>(API_URL.company + '/auth/piani');
  }

  public verifyAccessToken(token: string): Observable<loginSuccess> {
    return this.http.get<loginSuccess>(API_URL.auth + '/auth/verifyAccessToken/' + token, {
      context: new HttpContext().set(SKIP_AUTH_ERROR, true),
    });
  }
  public verifyRefreshToken(token: string): Observable<loginSuccess> {
    return this.http.get<loginSuccess>(API_URL.auth + '/auth/verifyRefreshToken/' + token);
  }
}
