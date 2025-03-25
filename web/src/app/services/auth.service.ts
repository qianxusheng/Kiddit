import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

interface AuthResponse {
  message: string;
  status: string;
  token?: string; 
}

@Injectable({
  providedIn: 'root' 
})

export class AuthService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient, private router: Router) {}

  // Register a new user
  register(user: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, user);
  }

  // Login an existing user
  login(credentials: any): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, credentials);
  }

  logout() {
    localStorage.removeItem('token'); 
    this.router.navigate(['/auth']); 
  }
  
  isLoggedIn(): boolean {
    return !!localStorage.getItem('token');
  }

  storeToken(token: string): void {
    localStorage.setItem('token', token);
  }
}