import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';

export interface LoginResponse {
  firstName: string;
  lastName: string;
  userId: number;
  token: string; 
}

export interface RegisterResponse {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
}

export interface RegisterRequest {
  nickName: string;
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export interface LoginRequest {
  email: string;
  password: string;
}

export interface UserInfo {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
}


@Injectable({
  providedIn: 'root' 
})

export class AuthService {
  private apiUrl = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient, private router: Router) {}

  // Register a new user
  register(user: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.apiUrl}/register`, user);
  }

  // Login an existing user
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials);
  }

  getUserInfo(userId: number): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.apiUrl}/info?userId=${userId}`);
  }

  logout() {
    localStorage.removeItem('token'); 
    localStorage.removeItem('userId'); 
    this.router.navigate(['/login']); 
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token'); 
  }
}