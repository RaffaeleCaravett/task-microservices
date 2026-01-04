import { Component, inject, OnInit, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { NavComponent } from './components/nav/nav';
import { FootComponent } from './components/foot/foot';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavComponent, FootComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App implements OnInit {
  protected readonly title = signal('task-fe');
  private authService: AuthService = inject(AuthService);
  ngOnInit(): void {
    let token: string | null = localStorage.getItem('accessToken');
    let refresh: string | null = localStorage.getItem('refreshToken');
    if (token) {
    }
  }
}
