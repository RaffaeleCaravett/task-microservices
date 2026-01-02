import { Routes } from '@angular/router';
import { HomeRoutes } from './components/home/home.routes';

export const routes: Routes = [
  {
    path: 'home',
    loadChildren: () => Promise.resolve(HomeRoutes),
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full',
  },
];
