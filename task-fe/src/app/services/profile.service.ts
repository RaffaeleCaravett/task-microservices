import { HttpClient } from '@angular/common/http';
import { EnvironmentInjector, inject, Injectable } from '@angular/core';
import { API_URL } from '../core/environment';
import { map, Observable } from 'rxjs';
import { Page, projectType, User } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root',
})
export class ProfileService {
  private http: HttpClient = inject(HttpClient);

  findUsersByCompanyAndFilters(
    companyId: number,
    filters: { email?: string; fullname?: string; status?: string },
    page: number,
    size: number,
  ): Observable<Page<User>> {
    return this.http
      .post<
        Page<User>
      >(API_URL.general + '/users/byCompanyAndFilters/' + companyId + '?page=' + page + '&size=' + size, filters)
      .pipe(map((u) => this.addColor(u)));
  }

  addColor(u: Page<User>): Page<User> {
    u.content.forEach((us: User) => {
      var color = this.getColor();
      us.color = color;
    });
    return u;
  }
  getColor(): string {
    var backgrounds: string[] = [
      'bg-light',
      'bg-warning',
      'bg-success',
      'bg-danger',
      'bg-primary',
      'bg-info',
    ];

    var random = Math.floor(Math.random() * backgrounds.length);
    if (random > backgrounds.length) {
      random = random - 1;
    }
    return backgrounds[random];
  }

  getProjectTypes(): Observable<projectType[]> {
    return this.http.get<projectType[]>(API_URL.general + '/company/types');
  }
}
