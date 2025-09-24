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
  isLogin: boolean = true; // Flag to toggle between login and registration forms
  errorMessage: string | null = null; // Error message shown for login/register failures

  loginForm: FormGroup;    // FormGroup instance for login form
  registerForm: FormGroup; // FormGroup instance for registration form

  constructor(
    private fb: FormBuilder, // FormBuilder service for easier form creation
    private authService: AuthService, // Custom authentication service
    private router: Router, // Angular Router to navigate after login
    private snackBar: MatSnackBar // Snackbar for showing success/failure messages
  ) {
    // Initialize login form with validators
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });

    // Initialize registration form with validators
    this.registerForm = this.fb.group({
      nickName: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required]
    });
  }

  /**
   * Handles login form submission
   */
  onLoginSubmit() {
    if (this.loginForm.valid) {
      const loginData: LoginRequest = this.loginForm.value;

      this.authService.login(loginData).subscribe(
        (response) => {
          // Save token and userId in localStorage after successful login
          localStorage.setItem('token', response.token);
          localStorage.setItem('userId', String(response.userId));

          // Redirect to homepage
          this.router.navigate(['/home']);
        },
        (error) => {
          // Show error message if login fails
          this.errorMessage = 'Invalid email or password.';
        }
      );
    }
  }

  /**
   * Handles registration form submission
   */
  onRegisterSubmit() {
    if (this.registerForm.valid) {
      const registerData: RegisterRequest = this.registerForm.value;

      this.authService.register(registerData).subscribe(
        (response) => {
          // Show success snackbar
          this.snackBar.open('Registration successful!', 'Close', {
            duration: 3000,
            verticalPosition: 'top',
            horizontalPosition: 'center',
          });

          // Reload the page after 2 seconds to switch to login
          setTimeout(() => {
            window.location.reload();
          }, 2000);
        },
        (error) => {
          // Show error message if registration fails
          this.errorMessage = 'Registration failed. Please try again.';
        }
      );
    }
  }

  /**
   * Toggles between login and register forms
   */
  toggleForm() {
    this.isLogin = !this.isLogin;
    this.errorMessage = null; // Clear any previous error messages
  }
}
