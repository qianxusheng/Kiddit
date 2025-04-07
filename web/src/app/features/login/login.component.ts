import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { LoginRequest, RegisterRequest } from '../../services/auth.service';
import { ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule]
})

export class LoginComponent {
  isLogin: boolean = true;
  errorMessage: string | null = null;

  loginForm: FormGroup;
  registerForm: FormGroup;

  constructor(private fb: FormBuilder, 
              private authService: AuthService, 
              private router: Router,
              private snackBar: MatSnackBar) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

    this.registerForm = this.fb.group({
      nickName: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  onLoginSubmit() {
    if (this.loginForm.valid) {
      const loginData: LoginRequest = this.loginForm.value;
      this.authService.login(loginData).subscribe(
        (response) => {
          localStorage.setItem('token', response.token);
          localStorage.setItem('userId', String(response.userId));
          this.router.navigate(['/home']);
        },
        (error) => {
          this.errorMessage = 'Invalid email or password.';
        }
      );
    }
  }

  onRegisterSubmit() {
    if (this.registerForm.valid) {
      const registerData: RegisterRequest = this.registerForm.value;
      this.authService.register(registerData).subscribe(
        (response) => {
          this.snackBar.open('Registration successful!', 'Close', {
            duration: 3000,  
            verticalPosition: 'top',  
            horizontalPosition: 'center', 
          });

          setTimeout(() => {
            window.location.reload();
          }, 2000); 
      },
      (error) => {
        this.errorMessage = 'Registration failed. Please try again.';
      }
    );}
  }

  toggleForm() {
    this.isLogin = !this.isLogin;
    this.errorMessage = null;
  }
}
