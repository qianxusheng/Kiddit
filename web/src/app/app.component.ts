import { Component, ChangeDetectorRef  } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AuthService, UserInfo } from './services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatMenuModule } from '@angular/material/menu';

@Component({
  selector: 'app-root',
  imports: [RouterModule, CommonModule, MatButtonModule, MatMenuModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})

export class AppComponent implements OnInit {
  title = 'kiddit';
  userId: number | null = null;
  userInfo: UserInfo | null = null;
  showToolbar = true;
  isLoginPage = false;

  constructor(private authService: AuthService,
              private router: Router,
              private cdr: ChangeDetectorRef) {}

  ngOnInit() {
    this.router.events.subscribe(() => {
      this.showToolbar = this.router.url !== '/login'; 
      this.isLoginPage = this.router.url == '/login'; 
    });

    this.userId = Number(localStorage.getItem('userId'));
    if (this.userId) {
      this.authService.getUserInfo(this.userId).subscribe(
        (userInfo) => {
          this.userInfo = userInfo;
          this.cdr.detectChanges();
        },
        (error) => {
          console.error('Error fetching user info:', error);
        }
      );
    } else {
      console.error('User ID not found in localStorage');
    }
  }

  onLogout() {
    this.authService.logout();
  }

  onHome() {
    this.router.navigate(['/home']);
  }

  onProfile() {
    this.router.navigate(['/profile/' + this.userId]);
  }
}
