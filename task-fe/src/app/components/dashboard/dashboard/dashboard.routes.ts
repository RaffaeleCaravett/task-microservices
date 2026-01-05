import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard';
import { ProfileComponent } from '../profile/profile';
import { AuthGuard } from '../../../core/auth.guard';

export const DashboardRoutes: Routes = [
  {
    path: '',
    redirectTo: 'landing',
    pathMatch: 'full',
  },
  {
    path: '',
    component: DashboardComponent,
    canActivate: [AuthGuard],
    canActivateChild: [AuthGuard],
    children: [
      {
        path: 'landing',
        component: ProfileComponent,
      },
    ],
  },
];
