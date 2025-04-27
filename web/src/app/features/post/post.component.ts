import { Component, OnInit } from '@angular/core';
import { PostService, PostDTO, CommentDTO, SubCommentDTO, VoteType } from '../../services/post.service';
import { MatPaginator } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { FormsModule } from '@angular/forms'; 
import { Location } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { SuggestionDialogComponent } from './suggestion-dialog.component';  

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  imports: [MatPaginator, CommonModule, MatIconModule, FormsModule],
  styleUrls: ['./post.component.scss']
})

export class PostComponent implements OnInit {
  userId!: number;
  VoteType = VoteType;
  comments: CommentDTO[] = []; // List of main comments
  subCommentsMap = new Map<number, SubCommentDTO[]>(); // Maps commentId to its list of sub-comments
  subCommentsPageMap = new Map<number, number>(); // Tracks the current page for sub-comments of each comment
  totalSubCommentsMap = new Map<number, number>(); // Stores total number of sub-comments per comment
  totalComments: number = 0; // Total number of main comments
  page: number = 1; // Current page for main comments
  pageSize: number = 10; // Number of main comments per page
  postId!: number; // ID of the current post
  post!: PostDTO; // Post details
  commentContent = '';
  subCommentContent = '';
  showSubCommentInput: { [commentId: number]: boolean } = {};
  suggestion?: string;  
  label?: string; 

  constructor(
    private postService: PostService, // Service for fetching post and comment data
    private route: ActivatedRoute, // To get the post ID from the route
    private location: Location,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.userId = Number(localStorage.getItem('userId'));
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

  // Method to handle upvoting a comment
  upvoteComment(comment: any) {
    // If the user has already upvoted, cancel the vote
    if (comment.userVoteStatus === 'UP') {
      comment.upvotes--;
      comment.userVoteStatus = ''; // Clear user's vote status
    } else {
      // If the user has downvoted, cancel the downvote
      if (comment.userVoteStatus === 'DOWN') {
        comment.downvotes--;
      }
      comment.upvotes++; // Increment upvotes
      comment.userVoteStatus = 'UP'; // Set user's vote status to 'UP'
    }

    // Call backend API to update the vote
    this.postService.voteComment(comment.commentId, this.userId, VoteType.UP).subscribe({
      error: (err) => {
        // Revert UI changes if API call fails
        if (comment.userVoteStatus === 'UP') {
          comment.upvotes--;
          comment.userVoteStatus = '';
        } else {
          comment.upvotes++;
          comment.userVoteStatus = 'UP';
        }
        console.error('Failed to upvote comment:', err);
      }
    });
  }

  // Method to handle downvoting a comment
  downvoteComment(comment: any) {
    // If the user has already downvoted, cancel the vote
    if (comment.userVoteStatus === 'DOWN') {
      comment.downvotes--;
      comment.userVoteStatus = ''; // Clear user's vote status
    } else {
      // If the user has upvoted, cancel the upvote
      if (comment.userVoteStatus === 'UP') {
        comment.upvotes--;
      }
      comment.downvotes++; // Increment downvotes
      comment.userVoteStatus = 'DOWN'; // Set user's vote status to 'DOWN'
    }

    // Call backend API to update the vote
    this.postService.voteComment(comment.commentId, this.userId, VoteType.DOWN).subscribe({
      error: (err) => {
        // Revert UI changes if API call fails
        if (comment.userVoteStatus === 'DOWN') {
          comment.downvotes--;
          comment.userVoteStatus = '';
        } else {
          comment.downvotes++;
          comment.userVoteStatus = 'DOWN';
        }
        console.error('Failed to downvote comment:', err);
      }
    });
  }

  goBack(): void {
    this.location.back();
  }

  // Add sub-comment to a specific comment
  onAddSubComment(commentId: number): void {
    if (!this.subCommentContent.trim()) return;

    this.postService.addSubComment(commentId, this.userId, this.subCommentContent)
      .subscribe({
        next: (response) => {
          if ('label' in response && response.label != 'Appropriate') {
            this.suggestion = response.suggestion;
            this.label = response.label;
            this.showSuggestionDialog();
          } else {
            this.subCommentContent = '';
            // Reload sub-comments from page 0
            this.subCommentsMap.set(commentId, []); // Clear current map
            this.loadSubComments(commentId, 0);
          }
        },
        error: (err) => console.error('Error adding sub-comment', err)
      });
  }

  // Add main comment to post
  onAddComment(): void {
    if (!this.commentContent.trim()) return;
  
    this.postService.addComment(this.postId, this.userId, this.commentContent)
      .subscribe({
        next: (response) => {
          if ('label' in response && response.label != 'Appropriate') {
            this.suggestion = response.suggestion;
            this.label = response.label;
            this.showSuggestionDialog();
          } else {
            this.commentContent = '';
            this.loadComments(); 
          }
        },
        error: (err) => console.error('Error adding comment', err)
      });
  }

  private showSuggestionDialog(): void {
    const dialogRef = this.dialog.open(SuggestionDialogComponent, {
      data: { 
        label: this.label, 
        suggestion: this.suggestion 
      }
    });
  
    dialogRef.afterClosed().subscribe(() => {
      this.suggestion = undefined;
      this.label = undefined;
    });
  }

  toggleSubCommentInput(commentId: number) {
    this.showSubCommentInput[commentId] = !this.showSubCommentInput[commentId];
    if (!this.showSubCommentInput[commentId]) {
      this.subCommentContent = '';
    }
  }
}
