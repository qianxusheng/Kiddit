import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface PageResponse<T> {
    content: T[];     
    totalElements: number; 
    totalPages: number;  
    size: number;        
    number: number;    
  }

export interface PostDTO {
    postId: number;
    subject: string;
    description: string;
    createdByFirstName: string;
    createdByLastName: string;
    createdAt: string;
    userId: number;
  }

export interface CommentDTO {
  commentId: number;
  content: string;
  createdByFirstName: string;
  createdByLastName: string;
  createdAt: string;
}

export interface SubCommentDTO {
  subCommentId: number;
  content: string;
  createdByFirstName: string;
  createdByLastName: string;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})

export class PostService {
  private apiUrl = 'http://localhost:8080/api';  // API base URL

  constructor(private http: HttpClient) {}

  // Fetches posts for a specific subKiddit with pagination
  getPostsBySubKiddit(subKidditId: number, page: number, size: number): Observable<PageResponse<PostDTO>> {
    return this.http.get<PageResponse<PostDTO>>(`${this.apiUrl}/${subKidditId}/posts`, {
        params: {
            page: page.toString(), // Page number as query param
            size: size.toString()  // Items per page as query param
        }
    });
  }

  // Create a new post in a specific subKiddit
  // Takes subKidditId and post details as parameters
  // Returns an Observable of PostDTO
  createPost(subKidditId: number, post: PostDTO): Observable<PostDTO> {
    return this.http.post<PostDTO>(`${this.apiUrl}/${subKidditId}/posts`, post);
  }
  
  // Fetches comments for a specific post with pagination
  getComments(postId: number, page: number, size: number): Observable<PageResponse<CommentDTO>> {
    return this.http.get<PageResponse<CommentDTO>>(`${this.apiUrl}/posts/${postId}/comments`, {
        params: {
          page: page.toString(), // Page number as query param
          size: size.toString()  // Items per page as query param
        }
      });
  }

  // Fetches subcomments for a specific comment with pagination
  getSubComments(commentId: number, page: number, size: number): Observable<PageResponse<SubCommentDTO>> {
    return this.http.get<PageResponse<SubCommentDTO>>(`${this.apiUrl}/comment/${commentId}/subcomments`, {
      params: {
        page: page.toString(), // Page number as query param
        size: size.toString()  // Items per page as query param
      }
    });
  }
  
  // Fetches a specific post by its ID
  getPostById(postId: number): Observable<PostDTO> {
    return this.http.get<PostDTO>(`${this.apiUrl}/posts/${postId}`);
  }
}