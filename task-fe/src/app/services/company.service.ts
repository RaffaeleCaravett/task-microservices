import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { API_URL } from '../core/environment';
import { map, Observable } from 'rxjs';
import { accessCode, CompanyProjectsFilters, Page, project, User } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root',
})
export class CompanyService {
  private http: HttpClient = inject(HttpClient);

  getUsers(
    companyId: number,
    filters: { page: number; size: number },
    skipFilters?: boolean,
  ): Observable<Page<User>> {
    return this.http
      .get<Page<User>>(
        API_URL.general +
          '/company/users/' +
          companyId +
          `?page=${filters.page}&size=${filters.size}`,
      )
      .pipe(map((u) => this.filterUsersPage(u, skipFilters)))
      .pipe(map((u) => this.addColor(u)));
  }


   getUserList(
    companyId: number,
  ): Observable<User[]> {
    return this.http
      .get<User[]>(
        API_URL.general +
          '/company/users/list/' +
          companyId,
      );
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

  getCompanyProjects(
    filters: CompanyProjectsFilters,
    companyId: number,
  ): Observable<Page<project>> {
    // let requestParams: string = '';
    // Object.entries(filters).forEach(([key, value]) => {
    //   if (value) {
    //     requestParams += `${key}=${value}` + '&';
    //   }
    // });
    // requestParams = requestParams.substring(0, requestParams.length - 1);
    return this.http.post<Page<project>>(
      API_URL.general +
        '/project/' +
        companyId +
        `?page=${filters.page || 0}&size=${filters.size || 10}&sort=${filters.sort || 'id'}&order=${filters.order || 0}`,
      filters,
    );
  }

  markAsFavourite(id:number):Observable<project>{
    return this.http.get<project>(API_URL.general + '/project/favourite/'+id);
  }
  unmarkAsFavourite(id:number):Observable<project>{
    return this.http.get<project>(API_URL.general + '/project/unfavourite/'+id);
  }
}
