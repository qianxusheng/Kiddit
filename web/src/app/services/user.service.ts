import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders  } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PageResponse<T> {
  content: T[];     
  totalElements: number; 
  totalPages: number;  
  size: number;        
  number: number;    
}

export interface CategoryDTO {
  categoryId: number;
  subject: string;
  description: string;
}

export interface SubKidditDTO {
  subkidditId: number;
  subject: string;
  description: string;
  categoryId: number;
  categoryName: string;
  createdAt: string;
  createdByUserId: number;
  createdByFirstName: string;
  createdByLastName: string;
}

export interface UserInfo {
  userId: number;
  firstName: string;
  lastName: string;
  email: string;
}

export interface UserProfileDTO {
  userId: number;
  bio: string;
  avatarUrl: string;
}

export interface RecentCommentDTO {
  commentId: number;
  content: string;
  createdAt: string; 
  userId: number;
  postSubject: string; 
  postCreatedAt: string;  
}

@Injectable({
  providedIn: 'root'
})

export class UserService {
  private apiUrl = 'http://localhost:8080/api'; // Base URL for API requests

  constructor(private http: HttpClient) {}

  private getToken(): string | null {
    return localStorage.getItem('token'); 
  }

  private getAuthHeaders(): HttpHeaders {
    const token = this.getToken();
    return new HttpHeaders({
      'Authorization': token ? `Bearer ${token}` : ''
    });
  }

  // Fetches all categories for a specific user
  getUserAllCategories(userId: number): Observable<CategoryDTO[]> {
    return this.http.get<CategoryDTO[]>(`${this.apiUrl}/users/${userId}/categories/all`, {
      headers: this.getAuthHeaders()
    }); 
  }

  // Fetches paginated categories for a specific user
  getUserPagedCategories(userId: number, page: number, size: number): Observable<PageResponse<CategoryDTO>> {
    return this.http.get<PageResponse<CategoryDTO>>(`${this.apiUrl}/users/${userId}/categories`, {
        params: {
          page: page.toString(),
          size: size.toString()
        },
        headers: this.getAuthHeaders()
    });
  }

  // Fetches profile information for a specific user
  getUserProfile(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/profile/${userId}`,{ 
        headers: this.getAuthHeaders()
      });
  }

  // Fetches paginated SubKiddits for a specific category
  getSubKidditsByCategory(categoryId: number, page: number, size: number): Observable<PageResponse<SubKidditDTO>> {
    return this.http.get<PageResponse<SubKidditDTO>>(
      `${this.apiUrl}/subkiddits/category/${categoryId}?page=${page}&size=${size}`,{
        headers: this.getAuthHeaders()});
  }

  // Removes a category from a user (not used yet)
  removeCategory(userId: number, categoryId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${userId}/categories/${categoryId}`, {
      headers: this.getAuthHeaders()}); 
  }

  // Fetches user information by userId from the server
  getUserInfo(userId: number): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.apiUrl}/users/info?userId=${userId}`, {
      headers: this.getAuthHeaders()});
  }

  /**
   * Get all available categories
   */
  getAllCategories(): Observable<CategoryDTO[]> {
    return this.http.get<CategoryDTO[]>(`${this.apiUrl}/categories`, {
      headers: this.getAuthHeaders()});
  }

  /**
 * Add categories to the user's profile
 * @param userId the ID of the user
 * @param categoryIds list of category IDs to be added
 */
  addUserCategories(userId: number, categoryIds: number[]): Observable<any> {
    const body = { categoryIds };
    return this.http.put(`${this.apiUrl}/profile/${userId}/categories`, body, {
      headers: this.getAuthHeaders()});
  }

  /**
   * Retrieves the avatar of the user based on the user profile ID.
   * @param userProfileId The ID of the user profile
   * @returns Observable<Blob> The avatar image as a Blob
   */
  getUserAvatar(userProfileId: number): Observable<Blob> {
    const url = `${this.apiUrl}/avatars/user-profile/${userProfileId}`;
    return this.http.get(url, { responseType: 'blob', headers: this.getAuthHeaders()});
  }

  /**
   * Uploads a new avatar and associates it with the given user profile.
   * @param profileId The ID of the user profile
   * @param file The uploaded avatar file
   * @returns Observable<string> A success message after uploading the avatar
   */
  uploadUserAvatar(profileId: number, file: File): Observable<string> {
    const url = `${this.apiUrl}/avatars/upload/${profileId}`;
    const formData = new FormData();
    formData.append('file', file, file.name);
    return this.http.post<string>(url, formData, { headers: this.getAuthHeaders() });
  }

  /**
 * Updates the user's bio with the provided content.
 * @param userId The ID of the user whose bio is to be updated
 * @param bio The new bio content
 * @returns Observable<void> A response indicating the success of the bio update
 */
  updateUserBio(userId: number, bio: string): Observable<void> {
    const url = `${this.apiUrl}/profile/${userId}/bio`;
    return this.http.put<void>(url, bio, { headers: this.getAuthHeaders() });
  }

  /**
   * Get avatar image by user profile ID
   * @param userProfileId the user's profile ID
   * @returns Observable with avatar image data as a Blob
   */
  getAvatar(userProfileId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/avatars/user-profile/${userProfileId}`, {
      responseType: 'blob', headers: this.getAuthHeaders() 
    });
  }

  getRecentComments(userId: number): Observable<RecentCommentDTO[]> {
    return this.http.get<RecentCommentDTO[]>(`${this.apiUrl}/profile/${userId}/recent-comments`, { headers: this.getAuthHeaders() });
  }
}
