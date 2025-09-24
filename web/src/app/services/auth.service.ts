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

@Injectable({
  providedIn: 'root' // means this service is avaliable through the project
})

export class AuthService {
  private apiUrl = 'http://localhost:8080/api/users'; // Base URL for user-related API

  constructor(private http: HttpClient, private router: Router) {}

  // Registers a new user by sending user data to the server
  register(user: RegisterRequest): Observable<RegisterResponse> {
    return this.http.post<RegisterResponse>(`${this.apiUrl}/register`, user);
  }

  // Logs in an existing user by sending credentials to the server
  login(credentials: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(`${this.apiUrl}/login`, credentials);
  }

  // Fetches user information by userId from the server
  getUserInfo(userId: number): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.apiUrl}/info?userId=${userId}`);
  }

  // Logs the user out by removing token and userId from localStorage
  logout() {
    localStorage.removeItem('token'); // Remove token from localStorage
    localStorage.removeItem('userId'); // Remove userId from localStorage
    this.router.navigate(['/login']); // Redirect to login page
  }

  // Checks if the user is authenticated by the presence of a token in localStorage
  isAuthenticated(): boolean {
    return !!localStorage.getItem('token'); // Returns true if token is present
  }
}