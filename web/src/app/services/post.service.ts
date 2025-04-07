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
  private apiUrl = 'http://localhost:8080/api';  

  constructor(private http: HttpClient) {}

  getPostsBySubKiddit(subKidditId: number, page: number, size: number): Observable<PageResponse<PostDTO>> {
    return this.http.get<PageResponse<PostDTO>>(`${this.apiUrl}/${subKidditId}/posts`, {
        params: {
            page: page.toString(),
            size: size.toString()
          }
    });
  }
  
  getComments(postId: number, page: number, size: number): Observable<PageResponse<CommentDTO>> {
    return this.http.get<PageResponse<CommentDTO>>(`${this.apiUrl}/posts/${postId}/comments`, {
        params: {
          page: page.toString(),
          size: size.toString() 
        }
      });
  }

  getSubComments(commentId: number, page: number, size: number): Observable<PageResponse<SubCommentDTO>> {
    return this.http.get<PageResponse<SubCommentDTO>>(`${this.apiUrl}/comment/${commentId}/subcomments`, {
      params: {
        page: page.toString(),
        size: size.toString()
      }
    });
  }
  
  getPostById(postId: number): Observable<PostDTO> {
    return this.http.get<PostDTO>(`${this.apiUrl}/posts/${postId}`);
  }

}
