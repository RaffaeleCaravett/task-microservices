import { Routes } from '@angular/router';
import { HomeComponent } from './home';
import { LoginComponent } from '../login/login';
import { SignupComponent } from '../signup/signup';

export const HomeRoutes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full',
  },
  {
    path: '',
    component: HomeComponent,
    children: [
      {
        path: 'login',
        component: LoginComponent,
      },
      {
        path: 'signup',
        component: SignupComponent,
      },
    ],
  },
];
