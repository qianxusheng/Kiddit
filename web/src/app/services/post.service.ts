import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
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
    userVoteStatus?: VoteType | null;
    upvotes?: number;
    downvotes?: number;
  }

export interface CommentDTO {
  commentId: number;
  content: string;
  createdByFirstName: string;
  createdByLastName: string;
  createdAt: string;
  userVoteStatus?: VoteType | null;
  upvotes?: number;
  downvotes?: number;
}

export interface SubCommentDTO {
  subCommentId: number;
  content: string;
  createdByFirstName: string;
  createdByLastName: string;
  createdAt: string;
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

export enum VoteType {
  UP = 'UP',
  DOWN = 'DOWN'
}

export interface CommentResponseDTO {
  userId: number;
  content: string;
}

@Injectable({
  providedIn: 'root'
})

export class PostService {
  private apiUrl = 'http://localhost:8080/api';  // API base URL

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

  // Fetches posts for a specific subKiddit with pagination
  getPostsBySubKiddit(subKidditId: number, page: number, size: number): Observable<PageResponse<PostDTO>> {
    return this.http.get<PageResponse<PostDTO>>(`${this.apiUrl}/${subKidditId}/posts`, {
        params: {
            page: page.toString(), // Page number as query param
            size: size.toString()  // Items per page as query param
        },
        headers: this.getAuthHeaders()
    });
  }

  // Create a new post in a specific subKiddit
  // Takes subKidditId and post details as parameters
  // Returns an Observable of PostDTO
  createPost(subKidditId: number, post: PostDTO): Observable<PostDTO> {
    return this.http.post<PostDTO>(`${this.apiUrl}/${subKidditId}/posts`, post, {
      headers: this.getAuthHeaders()
    });
  }
  
  // Fetches comments for a specific post with pagination
  getComments(postId: number, page: number, size: number): Observable<PageResponse<CommentDTO>> {
    return this.http.get<PageResponse<CommentDTO>>(`${this.apiUrl}/${postId}/comments`, {
        params: {
          page: page.toString(), // Page number as query param
          size: size.toString()  // Items per page as query param
        },
        headers: this.getAuthHeaders()
      });
  }

  // Fetches subcomments for a specific comment with pagination
  getSubComments(commentId: number, page: number, size: number): Observable<PageResponse<SubCommentDTO>> {
    return this.http.get<PageResponse<SubCommentDTO>>(`${this.apiUrl}/comment/${commentId}/subcomments`, {
      params: {
        page: page.toString(), // Page number as query param
        size: size.toString()  // Items per page as query param
      },
      headers: this.getAuthHeaders()
    });
  }
  
  // Fetches a specific post by its ID
  getPostById(postId: number): Observable<PostDTO> {
    return this.http.get<PostDTO>(`${this.apiUrl}/posts/${postId}`,{
      headers: this.getAuthHeaders()
    });
  }

  votePost(postId: number, userId: number, voteType: VoteType): Observable<any> {
    const url = `${this.apiUrl}/posts/${postId}/votes`;
    const params = new HttpParams()
      .set('userId', userId.toString())  
      .set('voteType', voteType.toString());  
  
    return this.http.post(url, null, { headers: this.getAuthHeaders(), params });
  }

  voteComment(commentId: number, userId: number, voteType: VoteType): Observable<any> {
    const url = `${this.apiUrl}/comments/${commentId}/votes`;
    const params = new HttpParams()
      .set('userId', userId.toString())  
      .set('voteType', voteType.toString());  
  
    return this.http.post(url, null, { headers: this.getAuthHeaders(), params });
  }

  addComment(postId: number, userId: number, content: string): Observable<{ label: string, suggestion: string }> {
    const requestBody = { userId, content }; 
    return this.http.post<{ label: string, suggestion: string }>(
      `${this.apiUrl}/${postId}/comments`, 
      requestBody, 
      { headers: this.getAuthHeaders() }
    );
  }
  
  addSubComment(parentCommentId: number, userId: number, content: string): Observable<{ label: string, suggestion: string }> {
    const requestBody = { userId, content };
    return this.http.post<{ label: string, suggestion: string }>(
      `${this.apiUrl}/${parentCommentId}/sub-comments`, 
      requestBody, 
      { headers: this.getAuthHeaders() } 
    );
  }
}