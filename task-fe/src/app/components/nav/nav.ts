import { ChangeDetectorRef, Component, effect, inject, OnInit } from '@angular/core';
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
import { NgClass, NgStyle } from '@angular/common';
import { Tooltip } from 'primeng/tooltip';
import { MenuService } from '../../services/menu.service';

@Component({
  selector: 'app-nav',
  imports: [ToastModule, NgClass, Tooltip, NgStyle],
  templateUrl: './nav.html',
  styleUrl: './nav.scss',
  providers: [MessageService],
})
export class NavComponent implements OnInit {
  protected authService: AuthService = inject(AuthService);
  protected companyService: CompanyService = inject(CompanyService);
  protected router: Router = inject(Router);
  protected dialog: MatDialog = inject(MatDialog);
  private cdr: ChangeDetectorRef = inject(ChangeDetectorRef);
  protected user: User | null = null;
  protected company: Company | null = null;
  protected users!: Page<User>;
  protected messageService: MessageService = inject(MessageService);
  isDark: boolean = false;
  protected modeService: ModeService = inject(ModeService);
  protected menuService: MenuService = inject(MenuService);
  isLoggedIn: boolean = false;
  protected showMenu: boolean = false;
  protected menuVoices: { id: number; value: string; icon: string }[] = [];
  ngOnInit(): void {
    this.menuVoices = this.menuService.getMenu();
    this.showMenu = false;
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
      image = (this.authService.getCompany() as Company)?.immagine?.filter((i) => i.isCurrent)[0]
        ?.image;
    }
    if (!image || (image && image.length == 0)) {
      image = '/assets/logo/logo.png';
    }
    return image;
  }

  addNewProject() {
    const dialog = this.dialog.open(AddProjectComponent, { data: this.company });
  }
  protected toggleMode() {
    this.modeService.toggleMode();
  }
  constructor() {
    effect(() => {
      this.isDark = this.modeService.isDark();
    });
    effect(() => {
      this.isLoggedIn = this.authService.isLoggedIn();
      this.cdr.markForCheck();
    });
  }

  toggleMenu() {
    this.showMenu = !this.showMenu;
  }
  manageRoute(route: string) {}
}
