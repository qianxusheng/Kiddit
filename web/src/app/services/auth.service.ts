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

@Injectable({
  providedIn: 'root' // means this service is avaliable through the project
})

export class AuthService {
  private apiUrl = 'http://localhost:8080/api/users'; // Base URL for user-related API

  constructor(private http: HttpClient, private router: Router) {}

  // Registers a new user by sending user data to the server
  register(user: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.apiUrl}/auth/register`, user);
  }

  // Logs in an existing user by sending credentials to the server
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/auth/login`, credentials);
  }

  // Logs the user out by removing token and userId from localStorage
  logout() {
    localStorage.removeItem('token'); // Remove token from localStorage
    localStorage.removeItem('userId'); // Remove userId from localStorage
    this.router.navigate(['/login']); // Redirect to login page
  }

  getToken(){
    return localStorage.getItem('token');
  }

  // Checks if the user is authenticated by the presence of a token in localStorage
  isAuthenticated(): boolean {
    const token = this.getToken()
    if (!token) return false;
    return !this.isTokenExpired(token);
  }

  isTokenExpired(token: string): boolean {
    try {
      const payload = JSON.parse(atob(token.split('.')[1]));
      const now = Math.floor(Date.now() / 1000);
      return payload.exp < now;
    } catch {
      return true; 
    }
  }
}