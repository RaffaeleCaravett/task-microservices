import { Routes } from '@angular/router';
import { DashboardComponent } from './dashboard';
import { ProfileComponent } from '../profile/profile';
import { AuthGuard } from '../../../core/auth.guard';
import { ProjectsComponent } from '../projects/projects';
import { TeamComponent } from '../team/team';
import { SettingsComponent } from '../settings/settings';

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
      {
        path: 'progetti',
        component: ProjectsComponent,
      },
      {
        path: 'team',
        component: TeamComponent,
      },
      {
        path: 'landing/impostazioni',
        component: SettingsComponent,
      },
    ],
  },
];
