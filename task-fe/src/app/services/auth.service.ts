import { HttpClient } from '@angular/common/http';
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
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private isLoggedIn: boolean = false;
  private user: User | null = null;
  private company: Company | null = null;
  private accesstoken: string = '';
  private refreshtoken: string = '';
  private http: HttpClient = inject(HttpClient);
  public getIsLoggedIn(): boolean {
    return this.isLoggedIn;
  }
  public setIsLoggedIn(loggedIn: boolean): void {
    this.isLoggedIn = loggedIn;
  }
  public setUser(user: User): void {
    this.user = user;
  }
  public getUser(): any {
    return this.user;
  }
  public setCompany(company: Company): void {
    this.company = company;
  }
  public getCompany(): any {
    return this.company;
  }
  public setAccessToken(token: string) {
    this.accesstoken = token;
  }
  public setRefreshToken(token: string) {
    this.refreshtoken = token;
  }
  public getAccessToken(): string {
    return this.accesstoken;
  }
  public getRefreshToken(): string {
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
    return this.http.post<CompanyDTOFromSignup>(API_URL.company + '/company/signup', body);
  }
  public verifyCode(companyId: number, code: string): Observable<loginSuccess> {
    return this.http.get<loginSuccess>(
      API_URL.company + '/validate/code/' + code + '/' + companyId + '/COMPANY'
    );
  }
  public login(body: UserLogin): Observable<Token> {
    return this.http.post<Token>(API_URL.auth + '/auth/login', body);
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
}
