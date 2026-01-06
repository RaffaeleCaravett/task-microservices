import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { API_URL } from '../core/environment';
import { map, Observable } from 'rxjs';
import { accessCode, Page, User } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root',
})
export class CompanyService {
  private http: HttpClient = inject(HttpClient);

  getUsers(companyId: number, skipFilters?: boolean): Observable<Page<User>> {
    return this.http
      .get<Page<User>>(API_URL.general + '/company/users/' + companyId)
      .pipe(map((u) => this.filterUsersPage(u, skipFilters)))
      .pipe(map((u) => this.addColor(u)));
  }

  filterUsersPage(usersPage: Page<User>, skipFilters?: boolean) {
    if (skipFilters) return usersPage;
    var content = usersPage?.content;
    content = content?.filter((user) => user.isActive && user.isConfirmed);
    usersPage.content = content;
    return usersPage;
  }
  addColor(u: Page<User>): Page<User> {
    u.content.forEach((us: User) => {
      var color = this.getColor();
      us.color = color;
    });
    return u;
  }
  addUser(body: {}): Observable<accessCode> {
    return this.http.post<accessCode>(API_URL.general + '/company/user', body);
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
}
