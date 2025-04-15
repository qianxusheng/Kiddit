import { Component, OnInit } from '@angular/core';
import { UserService, CategoryDTO, SubKidditDTO, PageResponse } from '../../services/user.service';
import { MatPaginator } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms'; // Import FormsModule for ngModel

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  imports: [MatPaginator, CommonModule, RouterModule, FormsModule], // Include FormsModule here
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  // Placeholder for all user's categories
  categories: CategoryDTO[] = [];  

  // Stores SubKiddits filtered by category { categoryID: Subkiddits list}
  filteredSubKiddits: { [key: number]: SubKidditDTO[] } = {};  

  // Current page number for category pagination (starts from 1)
  pCategory: number = 1;  

  // Current page number for SubKiddit pagination {SubkidditID: currentPageNumber}
  pSubKiddit: { [key: number]: number } = {};  

  // Number of categories per page
  itemsPerPageCategory: number = 4;  

  // Number of SubKiddits per page
  itemsPerPageSubKiddit: number = 4;  

  // Total number of categories available (for pagination)
  totalItemsCategory: number = 0;  

  // Total number of SubKiddits available per category {categoryID: total number of subkiddits}
  totalItemsSubKiddit: { [key: number]: number } = {};  

  // Authentication token retrieved from localStorage
  token!: string | null; 

  // User ID retrieved from localStorage
  userId!: number;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    // Retrieve token and user ID from localStorage
    this.token = localStorage.getItem('token');
    this.userId = Number(localStorage.getItem('userId'));

    // If token exists, load categories; otherwise, log error
    if (this.token) {
      this.loadCategories();
    } else {
      console.error('User not logged in!');
    }
  }

  // Loads categories and their respective SubKiddits for the user
  loadCategories() {
    this.userService.getUserPagedCategories(this.userId, this.pCategory - 1, this.itemsPerPageCategory)
      .subscribe((data: PageResponse<CategoryDTO>) => {
        this.categories = data.content;  // all categories, is a list [category1, category2, ...]
        this.totalItemsCategory = data.totalElements;  // Total number of categories

        // For each category, load associated SubKiddits
        this.categories.forEach(category => {
          this.loadSubKiddits(category.categoryId, 0);
        });
      });
  }

  // Loads SubKiddits for a specific category and page
  loadSubKiddits(categoryId: number, page: number) {
    this.userService.getSubKidditsByCategory(categoryId, page, this.itemsPerPageSubKiddit)
      .subscribe((data: PageResponse<SubKidditDTO>) => {
        this.filteredSubKiddits[categoryId] = data.content;  // Store SubKiddits for the category
        this.totalItemsSubKiddit[categoryId] = data.totalElements;  // Total SubKiddits for this category

        // Set initial page for SubKiddits if not set
        if (!this.pSubKiddit[categoryId]) {
          this.pSubKiddit[categoryId] = 1;
        }
      });
  }

  // Handles pagination change for categories
  categoryPageChanged(event: any) {
    this.pCategory = event.pageIndex + 1;  // Update current page number
    this.loadCategories();  // Reload categories for the new page
  }

  // Handles pagination change for SubKiddits of a specific category
  subKidditPageChanged(event: any, categoryId: number) {
    this.pSubKiddit[categoryId] = event.pageIndex + 1;  // Update current page number 
    this.loadSubKiddits(categoryId, event.pageIndex);  // Reload SubKiddits for the category
  }
}
