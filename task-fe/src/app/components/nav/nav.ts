import { Component, inject } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router, RouterLinkActive, RouterLink } from '@angular/router';

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
}
