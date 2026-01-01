import { Routes } from '@angular/router';
import { DashboardRoutes } from './components/dashboard/dashboard/dashboard.routes';
import { HomeRoutes } from './components/home/home.routes';
import { AuthGuard } from './core/auth.guard';

export const routes: Routes = [
  {
    path: 'home',
    loadChildren: () => Promise.resolve(HomeRoutes),
  },
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
];
