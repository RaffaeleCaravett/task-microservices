import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { API_URL } from '../core/environment';
import { map, Observable } from 'rxjs';
import { Page, User } from '../interfaces/interfaces';

@Injectable({
  providedIn: 'root',
})
export class CompanyService {
  private http: HttpClient = inject(HttpClient);

  getUsers(companyId: number): Observable<Page<User>> {
    return this.http
      .get<Page<User>>(API_URL.general + '/company/users/' + companyId)
      .pipe(map((u) => this.filterUsersPage(u)));
  }

  filterUsersPage(usersPage:Page<User>){
    var content = usersPage.content;
    content = content?.filter((user) => user.isActive && user.isConfirmed);
    usersPage.content=content;
    return usersPage;
  }
}
