import { Component, OnInit } from '@angular/core';
import { UserService, CategoryDTO, SubKidditDTO, PageResponse } from '../../services/user.service';
import { MatPaginator } from '@angular/material/paginator';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  imports: [MatPaginator, CommonModule, RouterModule],
  styleUrls: ['./home.component.scss']
})

export class HomeComponent implements OnInit {
  categories: CategoryDTO[] = [];
  filteredSubKiddits: { [key: number]: SubKidditDTO[] } = {};  
  pCategory: number = 1;  
  pSubKiddit: { [key: number]: number } = {};  
  itemsPerPageCategory: number = 4;  
  itemsPerPageSubKiddit: number = 4;  
  totalItemsCategory: number = 0;  
  totalItemsSubKiddit: { [key: number]: number } = {};  
  token!: string| null;
  userId!: number;

  constructor(private userService: UserService){}

  ngOnInit(): void {
    this.token = localStorage.getItem('token');
    this.userId = Number(localStorage.getItem('userId'));
    if (this.token) {
      this.loadCategories();
    } else {
      console.error('User not logged in!');
    }
  }

  loadCategories() {
    this.userService.getUserPagedCategories(this.userId, this.pCategory - 1, this.itemsPerPageCategory).subscribe((data: PageResponse<CategoryDTO>) => {
      this.categories = data.content;
      this.totalItemsCategory = data.totalElements;

      this.categories.forEach(category => {
        this.loadSubKiddits(category.categoryId, 0);
      });
    });
  }

  loadSubKiddits(categoryId: number, page: number) {
    this.userService.getSubKidditsByCategory(categoryId, page, this.itemsPerPageSubKiddit).subscribe((data: PageResponse<SubKidditDTO>) => {
    
      this.filteredSubKiddits[categoryId] = data.content;
      this.totalItemsSubKiddit[categoryId] = data.totalElements;

      if (!this.pSubKiddit[categoryId]) {
        this.pSubKiddit[categoryId] = 1;
      }
    });
  }

  categoryPageChanged(event: any) {
    this.pCategory = event.pageIndex + 1;  
    this.loadCategories();  
  }

  subKidditPageChanged(event: any, categoryId: number) {
    this.pSubKiddit[categoryId] = event.pageIndex + 1;  
    this.loadSubKiddits(categoryId, event.pageIndex);  
  }
}
