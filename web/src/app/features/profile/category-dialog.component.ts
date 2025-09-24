import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

export interface Category {
  categoryId: number;
  subject: string;
}

@Component({
  selector: 'app-category-dialog',
  imports: [ CommonModule ],
  templateUrl: './category-dialog.component.html',
  styleUrls: ['./category-dialog.component.scss']
})

export class CategoryDialogComponent {
  availableCategories: Category[] = [];
  selectedCategoryIds: number[] = [];

  constructor(
    public dialogRef: MatDialogRef<CategoryDialogComponent>,
    @Inject(MAT_DIALOG_DATA)
    public data: { allCategories: Category[]; userFavoriteIds: number[] }
  ) {
    this.availableCategories = data.allCategories.filter(
      c => !data.userFavoriteIds.includes(c.categoryId)
    );
  }

  toggleSelection(categoryId: number): void {
    if (this.selectedCategoryIds.includes(categoryId)) {
      this.selectedCategoryIds = this.selectedCategoryIds.filter(id => id !== categoryId);
    } else {
      this.selectedCategoryIds.push(categoryId);
    }
  }

  confirmSelection(): void {
    this.dialogRef.close(this.selectedCategoryIds);
  }

  cancel(): void {
    this.dialogRef.close();
  }
}
