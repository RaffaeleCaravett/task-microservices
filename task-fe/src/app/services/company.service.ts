import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { API_URL } from '../core/environment';
import { map, Observable } from 'rxjs';
import { User } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root',
})
export class CompanyService {
  private http: HttpClient = inject(HttpClient);

  getUsers(companyId: number): Observable<User[]> {
    return this.http
      .get<User[]>(API_URL.general + '/company/users/' + companyId)
      .pipe(map((u) => u.filter((user) => user.isActive && user.isConfirmed)));
  }
}
