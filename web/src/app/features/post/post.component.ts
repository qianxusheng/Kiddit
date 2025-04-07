import { Component, OnInit } from '@angular/core';
import { PostService, PostDTO, CommentDTO, SubCommentDTO } from '../../services/post.service';
import { MatPaginator } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  imports: [MatPaginator, CommonModule],
  styleUrls: ['./post.component.scss']
})

export class PostComponent implements OnInit {
  comments: CommentDTO[] = [];
  subCommentsMap = new Map<number, SubCommentDTO[]>(); // key: commentId, value: subComments
  subCommentsPageMap = new Map<number, number>(); // key: commentId, value: current page for subComments
  totalSubCommentsMap = new Map<number, number>();
  totalComments: number = 0;
  page: number = 1;
  pageSize: number = 10;
  postId!: number;
  post!: PostDTO;

  constructor(
    private postService: PostService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.postId = +this.route.snapshot.paramMap.get('id')!;
    this.loadPost();
    this.loadComments();
  }

  loadPost(): void {
    this.postService.getPostById(this.postId).subscribe(post => {
      this.post = post;
    });
  }

  loadComments(): void {
    this.postService.getComments(this.postId, this.page - 1, this.pageSize).subscribe(response => {
      this.comments = response.content;
      this.totalComments = response.totalElements;
      // Load initial sub-comments for each comment
      this.comments.forEach(comment => {
        this.loadSubComments(comment.commentId, 0);
      });
    });
  }

  loadSubComments(commentId: number, page: number): void {
    if (!this.subCommentsPageMap.has(commentId)) {
      this.subCommentsPageMap.set(commentId, 0);
    }

    this.postService.getSubComments(commentId, page, 5).subscribe(response => {
      const existingSubComments = this.subCommentsMap.get(commentId) || [];
      this.subCommentsMap.set(commentId, [...existingSubComments, ...response.content]);
      this.subCommentsPageMap.set(commentId, page + 1);
      this.totalSubCommentsMap.set(commentId, response.totalElements); 
    });
  }

  onPageChange(event: any): void {
    this.page = event.pageIndex + 1;
    this.pageSize = event.pageSize;
    this.loadComments();
  }

  onLoadMoreSubComments(commentId: number): void {
    const currentPage = this.subCommentsPageMap.get(commentId) || 0;
    this.loadSubComments(commentId, currentPage);
  }

  getSubComments(commentId: number): SubCommentDTO[] {
    return this.subCommentsMap.get(commentId) || [];
  }

  canLoadMoreSubComments(commentId: number): boolean {
    const loadedSubComments = this.getSubComments(commentId).length;
    const totalSubComments = this.totalSubCommentsMap.get(commentId) || 0;
    return loadedSubComments < totalSubComments; 
  }
}
