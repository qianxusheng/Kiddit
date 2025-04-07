import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { ProfileComponent } from './features/profile/profile.component';
import { SubkidditComponent } from './features/subkiddit/subkiddit.component';
import { PostComponent } from './features/post/post.component';
import { LoginComponent } from './features/login/login.component';
import { AuthGuard } from './auth/auth.guard'; 


export const routes: Routes = [
    { path: 'login', component: LoginComponent},
    { path: 'home', component: HomeComponent, canActivate: [AuthGuard] },
    { path: 'profile/:userId', component: ProfileComponent, canActivate: [AuthGuard] },
    { path: 'subkiddit/:id', component: SubkidditComponent, canActivate: [AuthGuard] }, 
    { path: 'post/:id', component: PostComponent, canActivate: [AuthGuard] },
    { path: '', redirectTo: '/login', pathMatch: 'full' }
];
