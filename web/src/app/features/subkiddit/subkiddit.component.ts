import { Component, OnInit } from '@angular/core';
import { PostService, PostDTO } from '../../services/post.service';
import { MatPaginator } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-subkiddit',
  templateUrl: './subkiddit.component.html',
  imports: [MatPaginator, CommonModule, RouterModule],
  styleUrls: ['./subkiddit.component.scss']
})

export class SubkidditComponent implements OnInit {
  posts: PostDTO[] = [];  // Array to store the posts
  totalPosts: number = 0;  // Total number of posts
  page: number = 1;  // Current page number
  pageSize: number = 10;  // Number of posts per page
  subKidditId!: number;

  constructor(private postService: PostService, private route: ActivatedRoute) { }

  ngOnInit(): void {
    this.subKidditId = +this.route.snapshot.paramMap.get('id')!;
    console.log(this.subKidditId);  // Log the SubKiddit ID for debugging
    this.loadPosts();  // Load posts when the component initializes
  }

  // Method to load posts from the API
  loadPosts(): void {
    this.postService.getPostsBySubKiddit(this.subKidditId, this.page - 1, this.pageSize)  // Replace 1 with dynamic SubKiddit ID
      .subscribe(response => {
        this.posts = response.content;  // Store the posts in the array
        this.totalPosts = response.totalElements;  // Store the total number of posts
      });
  }

  // Method to handle page change event
  onPageChange(event: any): void {
    this.page = event.pageIndex + 1;  // Update the current page
    this.pageSize = event.pageSize;  // Update the number of posts per page
    this.loadPosts();  // Reload the posts with the new page parameters
  }
}
