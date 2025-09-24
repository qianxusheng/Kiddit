import { Component, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';

@Component({
  selector: 'app-profile',
  imports: [CommonModule],
  providers: [Router],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})
export class ProfileComponent implements OnInit {
  userProfile: any = null; // Stores user profile data
  userId!: number; // Logged-in user's ID
  categories: any[] = []; // User's selected categories

  constructor(
    private userService: UserService,
    private route: Router
  ) {}

  ngOnInit(): void {
    // Get userId from localStorage
    this.userId = Number(localStorage.getItem('userId'));

    if (!isNaN(this.userId)) {
      this.fetchUserProfile();  // Load user profile data
      this.loadCategories();    // Load user's selected categories
    } else {
      console.error('Invalid user ID');
    }
  }

  /**
   * Navigates back to the homepage
   */
  returnHome() {
    this.route.navigateByUrl('/home');
  }

  /**
   * Fetches user profile information from the backend
   */
  fetchUserProfile(): void {
    this.userService.getUserProfile(this.userId).subscribe({
      next: (profile) => {
        this.userProfile = profile;
      },
      error: (err) => {
        console.error('Error fetching user profile:', err);
      }
    });
  }

  /**
   * Loads all category selections for the current user
   */
  loadCategories() {
    this.userService.getUserAllCategories(this.userId).subscribe({
      next: (data: any) => {
        this.categories = data;
      },
      error: (err) => {
        console.error('Error fetching categories:', err);
      }
    });
  }

  /**
   * Removes a category from the user's preferences
   * @param categoryId - ID of the category to remove
   */
  removeCategory(categoryId: number) {
    this.userService.removeCategory(this.userId, categoryId).subscribe({
      next: () => {
        this.loadCategories(); // Reload the updated category list
      },
      error: (err) => {
        console.error('Error removing category:', err);
      }
    });
  }
}
