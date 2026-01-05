import { Component, inject, OnInit } from '@angular/core';
import { MenuService } from '../../../services/menu.service';
import { filter, map } from 'rxjs';
import { AuthService } from '../../../services/auth.service';
import { Company, User } from '../../../interfaces/interfaces';
import { DatePipe } from '@angular/common';
import { CompanyService } from '../../../services/company.service';

@Component({
  selector: 'app-profile',
  imports: [DatePipe],
  templateUrl: './profile.html',
  styleUrl: './profile.scss',
})
export class ProfileComponent implements OnInit {
  protected menuService: MenuService = inject(MenuService);
  protected companyService: CompanyService = inject(CompanyService);
  protected authService: AuthService = inject(AuthService);
  protected section: string = 'Generale';
  protected user: User | null = null;
  protected company: Company | null = null;
  protected users: User[] = [];
  ngOnInit(): void {
    this.menuService.setMenu([
      'Generale',
      'Monitora statistiche',
      'Cambio password',
      'Elimina profilo',
    ]);

    this.menuService
      .getVoice()
      .pipe(filter((v) => v != ''))
      .subscribe((data: string) => {
        this.section = data;
      });
    this.user = this.authService.getUser();
    this.company = this.authService.getCompany();

    if (this.company) {
      this.companyService
        .getUsers(this.company.id)
        .pipe(map((u) => u.filter((user) => user.isActive && user.isConfirmed)))
        .subscribe({
          next: (data: User[]) => {
            if (data) {
              this.users = data;
            }
          },
        });
    }
  }
  getImage(): string {
    var image: string = '';
    if (this.user) {
      image = this.user?.immagine?.filter((i) => i.isCurrent)[0]?.image;
    } else if (this.company) {
      image = this.company?.immagine?.filter((i) => i.isCurrent)[0]?.image;
    }
    if (!image || (image && image.length == 0)) {
      image = '/assets/logo/logo.png';
    }
    return image;
  }
}
