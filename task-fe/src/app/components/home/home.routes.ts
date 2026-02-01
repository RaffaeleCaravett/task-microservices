import { Routes } from '@angular/router';
import { HomeComponent } from './home';
import { LoginComponent } from '../login/login';
import { SignupComponent } from '../signup/signup';
import { ErrorComponent } from '../../core/error/error';

export const HomeRoutes: Routes = [
  {
    path: '',
    component: HomeComponent,
  }
];
