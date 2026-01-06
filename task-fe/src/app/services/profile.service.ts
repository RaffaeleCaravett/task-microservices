import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { API_URL } from '../core/environment';
import { Observable } from 'rxjs';
import { Page, User } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private http: HttpClient = inject(HttpClient);

  findUsersByCompanyAndFilters(
    companyId: number,
    filters: { email?: string; fullname?: string; status?: string },
    page: number,
    size: number
  ): Observable<Page<User>> {
    return this.http.post<Page<User>>(
      API_URL.general +
        '/users/byCompanyAndFilters/' +
        companyId +
        '?page=' +
        page +
        '&size=' +
        size,
      filters
    );
  }
}
