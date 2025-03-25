import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { ReactiveFormsModule } from '@angular/forms'; 
import { MatCardModule } from '@angular/material/card'; 
import { MatFormFieldModule } from '@angular/material/form-field'; 
import { MatInputModule } from '@angular/material/input'; 
import { MatButtonModule } from '@angular/material/button'; 
import { HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-auth',
  standalone: true,
  templateUrl: './auth.component.html',
  styleUrls: ['./auth.component.scss'],
  providers: [AuthService],
  imports:[CommonModule,
           ReactiveFormsModule, 
           MatCardModule, 
           MatFormFieldModule, 
           MatInputModule, 
           MatButtonModule,
           HttpClientModule
          ]
})
export class AuthComponent {
  isLoginMode = true;
  authForm!: FormGroup;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.authForm = this.fb.group({
      nickName: [''],
      firstName: [''],
      lastName: [''],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required] 
    });
  }

  switchMode(): void {
    this.isLoginMode = !this.isLoginMode;
    this.authForm.reset();
  }

  onSubmit(): void {
    if (this.authForm.invalid) {
      return;
    }

    const formData = this.authForm.value;

    const loginData = {
      email: formData.email,
      password: formData.password
    };

    if (this.isLoginMode) {
      this.authService.login(loginData).subscribe(
        () => {
          this.router.navigate(['/home']);
        },
        (error) => {
          console.error('Login failed', error);
        }
      );
    } else {
      this.authService.register(formData).subscribe(
        () => {
          this.router.navigate(['/home']);
        },
        (error) => {
          console.error('Registration failed', error);
        }
      );
    }
  }
}