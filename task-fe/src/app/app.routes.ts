import { Routes } from '@angular/router';
import { DashboardRoutes } from './components/dashboard/dashboard/dashboard.routes';
import { HomeRoutes } from './components/home/home.routes';
import { AuthGuard } from './core/auth.guard';
import { ErrorComponent } from './core/error/error';
import { LoginComponent } from './components/login/login';
import { SignupComponent } from './components/signup/signup';

export const routes: Routes = [
  {
    path: 'home',
    loadChildren: () => Promise.resolve(HomeRoutes),
  },
  {
    path: 'login',
    component: LoginComponent,
  },
  { path: 'signup/:email', component: SignupComponent },
  {
    path: 'dashboard',
    loadChildren: () => Promise.resolve(DashboardRoutes),
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
  {
    path: '**',
    component: ErrorComponent,
  },
];
