import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLinkActive, RouterLink } from '@angular/router';
import { Company, User } from '../../interfaces/interfaces';

@Component({
  selector: 'app-nav',
  imports: [RouterLinkActive, RouterLink],
  templateUrl: './nav.html',
  styleUrl: './nav.scss',
})
export class NavComponent {
  protected authService: AuthService = inject(AuthService);
  protected router: Router = inject(Router);

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
    this.router.navigate(['home/login']);
  }
  getImage(): string {
    var image: string = '';
    if (this.authService.getUser()) {
      image = (this.authService.getUser() as User)?.immagine?.filter((i) => i.isCurrent)[0]?.image;
    } else if (this.authService.getCompany()) {
      image = (this.authService.getCompany() as Company)?.immagine?.filter((i) => i.isCurrent)[0]
        ?.image;
    }
    if (!image || (image && image.length == 0)) {
      image = '/assets/logo/logo.png';
    }
    return image;
  }
}
