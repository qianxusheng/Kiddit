import { Component, OnInit } from '@angular/core';
import { PostService, PostDTO } from '../../services/post.service';
import { MatPaginator } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-subkiddit',
  templateUrl: './subkiddit.component.html',
  imports: [MatPaginator, CommonModule, RouterModule],
  styleUrls: ['./subkiddit.component.scss']
})
export class SubkidditComponent implements OnInit {
  posts: PostDTO[] = []; // Stores the list of posts retrieved from the backend
  totalPosts: number = 0; // Total number of posts (used for pagination)
  page: number = 1; // Current page index (starts from 1 for UI)
  pageSize: number = 10; // Number of posts to display per page
  subKidditId!: number; // ID of the selected SubKiddit (retrieved from route)

  constructor(
    private postService: PostService,
    private route: ActivatedRoute,
    private router: Router  // Inject Router to enable navigation
  ) {}

  ngOnInit(): void {
    // Get the SubKiddit ID from the URL parameter
    this.subKidditId = +this.route.snapshot.paramMap.get('id')!;
    console.log(this.subKidditId); // Debug log to verify SubKiddit ID

    // Load the posts for this SubKiddit
    this.loadPosts();
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
   * @param event the pagination event containing new page index and page size.
   */
  onPageChange(event: any): void {
    this.page = event.pageIndex + 1; // Angular Material Paginator uses 0-based index
    this.pageSize = event.pageSize;
    this.loadPosts(); // Reload posts for new page.
  }

  /**
   * Navigates to the "AddSubKiddit" page.
   * Triggered when the "AddSubKiddit" button is clicked.
   */
  addSubKiddit(): void {
    // Adjust the route as per your routing configuration.
    this.router.navigate(['/add-subkiddit']);
  }
}
