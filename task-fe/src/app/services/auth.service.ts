import { HttpClient } from '@angular/common/http';
import { Token } from '@angular/compiler';
import { inject, Injectable } from '@angular/core';
import {
  cap,
  citta,
  Company,
  CompanySignup,
  formaGiuridica,
  nazione,
  regione,
  settore,
  UserLogin,
} from '../interfaces/interfaces';
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
  public signup(body: CompanySignup): Observable<Company> {
    return this.http.post<Company>(API_URL.company + '/company/signup', body);
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
}
