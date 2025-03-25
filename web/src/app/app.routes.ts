import { Routes } from '@angular/router';
import { AuthComponent } from './components/auth/auth.component';
import { HomeComponent } from './components/home/home.component';
export const routes: Routes = [
    { path: 'auth', component: AuthComponent },
    { path: 'home', component: HomeComponent },
    { path: '', redirectTo: '/auth', pathMatch: 'full' }
];
