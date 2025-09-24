import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-suggestion-dialog',
  template: `
    <div class="dialog-container">
      <h1 mat-dialog-title class="dialog-title">Comment Issue</h1>
      <div mat-dialog-content class="dialog-content">
        <p><strong>Label:</strong> {{ data.label }}</p>
        <p><strong>Suggestion:</strong> {{ data.suggestion }}</p>
      </div>
      <div class="dialog-actions">
        <button mat-button (click)="closeDialog()" class="close-button">Close</button>
      </div>
    </div>
  `,
  styles: [
    `
      .dialog-container {
        background-color: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
        max-width: 500px;
        margin: 0 auto;
      }

      .dialog-title {
        font-size: 24px;
        font-weight: bold;
        color: #333;
        margin-bottom: 15px;
      }

      .dialog-content {
        font-size: 16px;
        color: #555;
        line-height: 1.5;
      }

      .dialog-actions {
        margin-top: 20px;
        text-align: right;
      }

      .close-button {
        background-color: #4caf50;
        color: white;
        border-radius: 4px;
        padding: 8px 16px;
        font-size: 14px;
        font-weight: 600;
        transition: background-color 0.3s ease;
      }

      .close-button:hover {
        background-color: #45a049;
      }

      .close-button:focus {
        outline: none;
      }
    `
  ]
})
export class SuggestionDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { label: string, suggestion: string },
    private dialogRef: MatDialogRef<SuggestionDialogComponent>
  ) {}

  closeDialog(): void {
    this.dialogRef.close();
  }
}
