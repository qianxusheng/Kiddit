import { Component, OnInit } from '@angular/core';
import { PostService, PostDTO, VoteType} from '../../services/post.service';
import { MatPaginator } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-subkiddit',
  templateUrl: './subkiddit.component.html',
  imports: [MatPaginator, CommonModule, RouterModule, FormsModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  styleUrls: ['./subkiddit.component.scss']
})

export class SubkidditComponent implements OnInit {
  VoteType = VoteType;
  posts: PostDTO[] = []; // Stores the list of posts retrieved from the backend
  totalPosts: number = 0; // Total number of posts (used for pagination)
  page: number = 1; // Current page index (starts from 1 for UI)
  pageSize: number = 10; // Number of posts to display per page
  subKidditId!: number; // ID of the selected SubKiddit (retrieved from route)
  userId!: number; // User ID retrieved from localStorage
  newPost = {
    subject: '',
    description: ''
  }; // Object to hold new post data for creation
  isCreatingPost: boolean = false; // Flag to indicate if a new post is being created
  showCreatePostForm: boolean = false; // Flag to toggle the post creation form

  constructor(
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.userId = Number(localStorage.getItem('userId'));
    // Get the SubKiddit ID from the URL parameter
    this.subKidditId = +this.route.snapshot.paramMap.get('id')!;
    // Load the posts for this SubKiddit
    this.loadPosts();
  }
  
  /**
   * Toggles the visibility of the post creation form.
   */
  createPost(): void {
    if (!this.newPost.subject || !this.newPost.description) return;
  
    this.isCreatingPost = true;

    const payload: PostDTO = {
      postId: 0, // placeholder, backend will assign
      subject: this.newPost.subject,
      description: this.newPost.description,
      createdByFirstName: '',
      createdByLastName: '',
      createdAt: '',
      userId: this.userId,
    };

    this.postService.createPost(this.subKidditId, payload).subscribe({
      next: (createdPost) => {
        this.isCreatingPost = false;
        this.showCreatePostForm = false;
        this.newPost = { subject: '', description: '' };
        this.loadPosts(); // Refresh post list
      },
      error: (err) => {
        this.isCreatingPost = false;
        console.error('Failed to create post:', err);
      }
    });
  }  

  /**
   * Loads a page of posts based on SubKiddit ID, page, and pageSize.
   */
  loadPosts(): void {
    this.postService.getPostsBySubKiddit(this.subKidditId, this.page - 1, this.pageSize)
      .subscribe(response => {
        this.posts = response.content; // Store retrieved posts
        this.totalPosts = response.totalElements; // Total number of posts available
      });
  }

  /**
   * Handles page change triggered by paginator.
   *
   * @param event the pagination event containing new page index and page size
   */
  onPageChange(event: any): void {
    this.page = event.pageIndex + 1; // Angular Material Paginator uses 0-based index
    this.pageSize = event.pageSize;
    this.loadPosts(); // Reload posts for new page
  }

  // Method to handle upvoting
  upvotePost(post: any) {
    // If the user has already upvoted, cancel the vote
    if (post.userVoteStatus === 'UP') {
      post.upvotes--; 
      post.userVoteStatus = ''; // Clear user's vote status
    } else {
      // If the user has downvoted, cancel the downvote
      if (post.userVoteStatus === 'DOWN') {
        post.downvotes--;
      }
      post.upvotes++; // Increment upvotes
      post.userVoteStatus = 'UP'; // Set user's vote status to 'UP'
    }

    // Call backend API to update the vote
    this.postService.votePost(post.postId, this.userId, VoteType.UP).subscribe();
  }

  // Method to handle downvoting
  downvotePost(post: any) {
    // If the user has already downvoted, cancel the vote
    if (post.userVoteStatus === 'DOWN') {
      post.downvotes--; 
      post.userVoteStatus = ''; // Clear user's vote status
    } else {
      // If the user has upvoted, cancel the upvote
      if (post.userVoteStatus === 'UP') {
        post.upvotes--;
      }
      post.downvotes++; // Increment downvotes
      post.userVoteStatus = 'DOWN'; // Set user's vote status to 'DOWN'
    }

    // Call backend API to update the vote
    this.postService.votePost(post.postId, this.userId, VoteType.DOWN).subscribe();
  }

  onPostClick(event: MouseEvent) {
    event.stopPropagation(); 
  }

  goBack(): void {
    this.router.navigate(['/home']);
  }
}
