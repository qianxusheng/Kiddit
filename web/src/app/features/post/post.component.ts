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
  comments: CommentDTO[] = []; // List of main comments
  subCommentsMap = new Map<number, SubCommentDTO[]>(); // Maps commentId to its list of sub-comments
  subCommentsPageMap = new Map<number, number>(); // Tracks the current page for sub-comments of each comment
  totalSubCommentsMap = new Map<number, number>(); // Stores total number of sub-comments per comment
  totalComments: number = 0; // Total number of main comments
  page: number = 1; // Current page for main comments
  pageSize: number = 10; // Number of main comments per page
  postId!: number; // ID of the current post
  post!: PostDTO; // Post details

  constructor(
    private postService: PostService, // Service for fetching post and comment data
    private route: ActivatedRoute // To get the post ID from the route
  ) {}

  ngOnInit(): void {
    // Get post ID from the route
    this.postId = +this.route.snapshot.paramMap.get('id')!;
    this.loadPost();     // Load post details
    this.loadComments(); // Load main comments for the post
  }

  /**
   * Fetches the post details by ID
   */
  loadPost(): void {
    this.postService.getPostById(this.postId).subscribe(post => {
      this.post = post;
    });
  }

  /**
   * Fetches main comments for the current post with pagination.
   * Also triggers loading of the first page of sub-comments for each main comment.
   */
  loadComments(): void {
    this.postService.getComments(this.postId, this.page - 1, this.pageSize).subscribe(response => {
      this.comments = response.content;
      this.totalComments = response.totalElements;

      // Load initial sub-comments for each main comment
      this.comments.forEach(comment => {
        this.loadSubComments(comment.commentId, 0);
      });
    });
  }

  /**
   * Loads sub-comments for a given comment ID and page.
   * Merges new sub-comments with previously loaded ones.
   */
  loadSubComments(commentId: number, page: number): void {
    // Initialize sub-comment page if not set
    if (!this.subCommentsPageMap.has(commentId)) {
      this.subCommentsPageMap.set(commentId, 0);
    }

    this.postService.getSubComments(commentId, page, 5).subscribe(response => {
      const existingSubComments = this.subCommentsMap.get(commentId) || [];
      this.subCommentsMap.set(commentId, [...existingSubComments, ...response.content]);

      // Update current page and total sub-comment count
      this.subCommentsPageMap.set(commentId, page + 1);
      this.totalSubCommentsMap.set(commentId, response.totalElements);
    });
  }

  /**
   * Handles page change event for the main comments pagination.
   */
  onPageChange(event: any): void {
    this.page = event.pageIndex + 1;
    this.pageSize = event.pageSize;
    this.loadComments();
  }

  /**
   * Loads more sub-comments for a specific comment.
   */
  onLoadMoreSubComments(commentId: number): void {
    const currentPage = this.subCommentsPageMap.get(commentId) || 0;
    this.loadSubComments(commentId, currentPage);
  }

  /**
   * Retrieves sub-comments for a specific comment from the map.
   */
  getSubComments(commentId: number): SubCommentDTO[] {
    return this.subCommentsMap.get(commentId) || [];
  }

  /**
   * Checks whether more sub-comments can be loaded for a specific comment.
   */
  canLoadMoreSubComments(commentId: number): boolean {
    const loadedSubComments = this.getSubComments(commentId).length;
    const totalSubComments = this.totalSubCommentsMap.get(commentId) || 0;
    return loadedSubComments < totalSubComments;
  }
}
