import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
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

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl = 'http://localhost:8080/api'; // Base URL for API requests

  constructor(private http: HttpClient) {}

  // Fetches all categories for a specific user
  getUserAllCategories(userId: number): Observable<CategoryDTO[]> {
    return this.http.get<CategoryDTO[]>(`${this.apiUrl}/users/${userId}/categories/all`); 
  }

  // Fetches paginated categories for a specific user
  getUserPagedCategories(userId: number, page: number, size: number): Observable<PageResponse<CategoryDTO>> {
    return this.http.get<PageResponse<CategoryDTO>>(`${this.apiUrl}/users/${userId}/categories`, {
        params: {
          page: page.toString(),
          size: size.toString()
        }
    });
  }

  // Fetches profile information for a specific user
  getUserProfile(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/profile/${userId}`);
  }

  // Fetches paginated SubKiddits for a specific category
  getSubKidditsByCategory(categoryId: number, page: number, size: number): Observable<PageResponse<SubKidditDTO>> {
    return this.http.get<PageResponse<SubKidditDTO>>(
      `${this.apiUrl}/subkiddits/category/${categoryId}?page=${page}&size=${size}`
    );
  }

  /**
   * Adds a new SubKiddit (community).
   * This method calls the backend POST endpoint for creating a new SubKiddit.
   *
   * @param subKidditDTO the SubKiddit data to be added.
   * @return an Observable of the created SubKidditDTO.
   */
  addSubKiddit(subKidditDTO: SubKidditDTO): Observable<SubKidditDTO> {
    return this.http.post<SubKidditDTO>(`${this.apiUrl}/subkiddits`, subKidditDTO);
  }

  // Removes a category from a user (not used yet)
  removeCategory(userId: number, categoryId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${userId}/categories/${categoryId}`); 
  }
}
