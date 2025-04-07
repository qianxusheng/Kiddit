import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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
  userProfile: any = null; 
  userId!: number;
  categories: any[] = []; 

  constructor(
    private userService: UserService,
    private route: Router
  ) {}

  ngOnInit(): void {
    this.userId = Number(localStorage.getItem('userId'));
    
    if (!isNaN(this.userId)) {
      this.fetchUserProfile();
      this.loadCategories(); 
    } else {
      console.error('Invalid user ID');
    }
  }

  returnHome() {
    this.route.navigateByUrl('/home');
  }

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

  removeCategory(categoryId: number) {
    this.userService.removeCategory(this.userId, categoryId).subscribe({
      next: () => {
        this.loadCategories(); 
      },
      error: (err) => {
        console.error('Error removing category:', err);
      }
    });
  }
}
