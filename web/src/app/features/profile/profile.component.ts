import { Component, OnInit, ViewChild, ElementRef  } from '@angular/core';
import { UserService, RecentCommentDTO } from '../../services/user.service';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { CategoryDialogComponent } from './category-dialog.component'; 
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';

@Component({  
  selector: 'app-profile',
  imports: [CommonModule, 
            FormsModule, 
            MatIconModule, 
            MatProgressSpinnerModule, 
            MatFormFieldModule, 
            MatInputModule
  ],
  standalone: true,
  providers: [Router],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss']
})

export class ProfileComponent implements OnInit {
  userProfile: any = null; // Stores user profile data
  userId!: number; // Logged-in user's ID
  userProfileId!: number;
  userCategories: any[] = []; // User's selected categories
  allCategories: any[] = []; 
  updatedUserBio: string = "";
  isEditing: boolean = false; // Flag to toggle edit mode
  selectedCategories: number[] = []; // To store selected categories
  showAllCategories: boolean = false; // Flag to toggle showing all categories
  avatarUrl!: string; // To store the avatar image URL
  recentComments: RecentCommentDTO[] = [];

  constructor(
    private userService: UserService,
    private route: Router,
    public dialog: MatDialog
  ) {}

  ngOnInit(): void {
    // Get userId from localStorage
    this.userId = Number(localStorage.getItem('userId'));
    this.fetchUserProfile();  
    this.loadUserCategories();    
    this.loadAllCategories();
    this.loadRecentComments();
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
        this.updatedUserBio = this.userProfile.bio; 
        this.userProfileId = this.userProfile.profileId
        this.loadAvatar();
      },
      error: (err) => {
        console.error('Error fetching user profile:', err);
      }
    });
  }

  /**
   * Loads all category selections for the current user
   */
  loadUserCategories() {
    this.userService.getUserAllCategories(this.userId).subscribe({
      next: (data: any) => {
        this.userCategories = data;
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
        this.loadUserCategories(); // Reload the updated category list
      },
      error: (err) => {
        console.error('Error removing category:', err);
      }
    });
  }

  /**
   * Updates the user's bio with the new data
   */
  updateUserBio(): void {
    this.userProfile.bio = this.updatedUserBio;
    this.userService.updateUserBio(this.userId, this.updatedUserBio).subscribe({
      next: () => {
        window.location.reload();
      },
      error: (err) => {
        console.error('Error updating bio:', err);
      }
    });
  }

  loadAllCategories(): void {
    this.userService.getAllCategories().subscribe({
      next: (categories) => {
        this.allCategories = categories;
      },
      error: (err) => {
        console.error('Error fetching categories:', err);
      }
    });
  }

  openAddCategoryDialog(): void {
    const dialogRef = this.dialog.open(CategoryDialogComponent, {
      width: '500px',
      data: {
        allCategories: this.allCategories,
        userFavoriteIds: this.userCategories.map((c: any) => c.categoryId)
      }
    });

    dialogRef.afterClosed().subscribe((selectedCategoryIds: number[]) => {
      if (selectedCategoryIds && selectedCategoryIds.length > 0) {
        const userId = this.userProfile.userId;
        this.userService.addUserCategories(userId, selectedCategoryIds).subscribe(() => {
          window.location.reload();
        });
      }
    });
  }

  // Access the <input type="file"> element from the template using the template reference variable #fileInput
  @ViewChild('fileInput') fileInputRef!: ElementRef<HTMLInputElement>;

  /**
   * Programmatically triggers the file input dialog by simulating a click on the hidden input element.
   */
  triggerFileSelect(): void {
    this.fileInputRef.nativeElement.click();
  }

  /**
   * Handles the file selection event when the user chooses an image.
   * It retrieves the selected file and initiates the upload process.
   * 
   * @param event - The change event from the file input
   */
  onAvatarSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];
      this.uploadUserAvatar(this.userProfileId, file);
    }
  }

  /**
   * Uploads the selected avatar file to the backend using the userService.
   * 
   * @param profileId - The profile ID associated with the avatar
   * @param file - The image file to be uploaded
   */
  uploadUserAvatar(profileId: number, file: File): void {
    this.userService.uploadUserAvatar(profileId, file).subscribe({
      next: (url: string) => {
        console.log('Avatar uploaded successfully:', url);
        window.location.reload(); // Reload the page to reflect the new avatar
      },
      error: (err) => {
        console.error('Error uploading avatar', err);
      }
    });
  }

  /**
   * Fetch the avatar image and create an image URL to display
   */
  loadAvatar(): void {
    this.userService.getAvatar(this.userProfileId).subscribe({
      next: (response: Blob) => {
        // Create an object URL from the Blob (image)
        if (response == null) {
          console.log('No avatar found, using default image');
          this.avatarUrl = 'logo.png';
        }
        else { 
            this.avatarUrl = URL.createObjectURL(response);
        }
      },
      error: (err) => {
        console.error('Error fetching avatar:', err);
      }
    });
  }

  loadRecentComments(): void {
    this.userService.getRecentComments(this.userId).subscribe(
      (comments: RecentCommentDTO[]) => {  
        this.recentComments = comments;
      },
      (error) => {
        console.error('Error loading recent comments:', error);
      }
    );
  }
}
