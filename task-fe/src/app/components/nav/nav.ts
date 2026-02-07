import { Component, effect, inject, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLinkActive, RouterLink } from '@angular/router';
import { Company, Page, User } from '../../interfaces/interfaces';
import { MatDialog } from '@angular/material/dialog';
import { AddProjectComponent } from '../../dialogs/add-project-component/add-project-component';
import { CompanyService } from '../../services/company.service';
import { ToastModule } from 'primeng/toast';
import { MessageService } from 'primeng/api';
import { take } from 'rxjs';
import { ModeService } from '../../services/mode.service';
import { NgClass } from '@angular/common';
import { Tooltip } from "primeng/tooltip";

@Component({
  selector: 'app-nav',
  imports: [RouterLinkActive, RouterLink, ToastModule, NgClass, Tooltip],
  templateUrl: './nav.html',
  styleUrl: './nav.scss',
  providers: [MessageService],
})
export class NavComponent implements OnInit {
  protected authService: AuthService = inject(AuthService);
  protected companyService: CompanyService = inject(CompanyService);
  protected router: Router = inject(Router);
  protected dialog: MatDialog = inject(MatDialog);
  protected user: User | null = null;
  protected company: Company | null = null;
  protected users!: Page<User>;
  protected messageService: MessageService = inject(MessageService);
  isDark: boolean = false;
  protected modeService: ModeService = inject(ModeService);
  ngOnInit(): void {
  }
  goToRoute(route: string) {
    this.router.navigate([`${route}`]);
  }
  logout() {
    localStorage.clear();
    this.authService.setIsLoggedIn(false);
    this.authService.setCompany(null);
    this.authService.setUser(null);
    this.authService.setAccessToken(null);
    this.authService.setRefreshToken(null);
    this.router.navigate(['home']);
  }
  getImage(): string {
    var image: string = '';
    if (this.authService.getUser()) {
      this.user = this.authService.getUser();
      image = (this.authService.getUser() as User)?.immagine?.filter((i) => i.isCurrent)[0]?.image;
    } else if (this.authService.getCompany()) {
      this.company = this.authService.getCompany();
      this.companyService.getUsers(this.company!.id).subscribe({
        next: (data: Page<User>) => {
          if (data) {
            this.users = data;
          }
        },
      });
      image = (this.authService.getCompany() as Company)?.immagine?.filter((i) => i.isCurrent)[0]
        ?.image;
    }
    if (!image || (image && image.length == 0)) {
      image = '/assets/logo/logo.png';
    }
    return image;
  }

  addNewProject() {
    if (this.users.content.length > 0) {
      const dialog = this.dialog.open(AddProjectComponent, { data: this.company });
    } else {
      this.messageService.add({
        severity: 'error',
        summary: 'Attention',
        detail: "You don't have users to add yet. Invite some users first!",
        life: 3000,
      });
    }
  }
  protected toggleMode(){
    this.modeService.toggleMode();
  }
  constructor() {
    effect(() => {
      this.isDark = this.modeService.isDark();
    });
  }
}
