import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CaseService } from '../../services/case.service';
import { CaseFormComponent } from '../case-form/case-form.component';
import { Case, CaseFormModel } from '../../models/case.model';

@Component({
  selector: 'app-case-list',
  standalone: true,
  imports: [CommonModule, CaseFormComponent],
  templateUrl: './case-list.component.html',
  styleUrls: ['./case-list.component.css']
})
export class CaseListComponent implements OnInit {
  cases: Case[] = [];
  selectedCase: Case | null = null;
  showForm = false;
  isEditMode = false;
  loading = false;
  error = '';

  constructor(private caseService: CaseService) {}

  ngOnInit() {
    this.loadCases();
  }

  loadCases() {
    this.loading = true;
    this.error = '';
    this.caseService.getAllCases().subscribe({
      next: (cases) => {
        this.cases = cases;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load cases';
        this.loading = false;
        console.error('Error loading cases:', error);
      }
    });
  }

  showCreateForm() {
    this.selectedCase = null;
    this.isEditMode = false;
    this.showForm = true;
  }

  editCase(caseItem: Case) {
    this.selectedCase = caseItem;
    this.isEditMode = true;
    this.showForm = true;
  }

  onFormSubmit(formData: CaseFormModel) {
    this.loading = true;
    this.error = '';

    if (this.isEditMode && this.selectedCase?.id) {
      // Update existing case
      this.caseService.updateCase(this.selectedCase.id, formData).subscribe({
        next: (updatedCase) => {
          const index = this.cases.findIndex(c => c.id === updatedCase.id);
          if (index !== -1) {
            this.cases[index] = updatedCase;
          }
          this.hideForm();
          this.loading = false;
        },
        error: (error) => {
          this.error = error.error || 'Failed to update case';
          this.loading = false;
          console.error('Error updating case:', error);
        }
      });
    } else {
      // Create new case
      this.caseService.createCase(formData).subscribe({
        next: (newCase) => {
          this.cases.unshift(newCase);
          this.hideForm();
          this.loading = false;
        },
        error: (error) => {
          this.error = error.error || 'Failed to create case';
          this.loading = false;
          console.error('Error creating case:', error);
        }
      });
    }
  }

  onFormCancel() {
    this.hideForm();
  }

  hideForm() {
    this.showForm = false;
    this.selectedCase = null;
    this.isEditMode = false;
  }

  deleteCase(caseItem: Case) {
    if (!caseItem.id) return;

    if (confirm(`Are you sure you want to delete case ${caseItem.courtCaseNumber || 'Unassigned'}?`)) {
      this.loading = true;
      this.error = '';

      this.caseService.deleteCase(caseItem.id).subscribe({
        next: () => {
          this.cases = this.cases.filter(c => c.id !== caseItem.id);
          if (this.selectedCase?.id === caseItem.id) {
            this.hideForm();
          }
          this.loading = false;
        },
        error: (error) => {
          this.error = 'Failed to delete case';
          this.loading = false;
          console.error('Error deleting case:', error);
        }
      });
    }
  }

  formatCurrency(amount: number | undefined): string {
    if (!amount) return '$0.00';
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD'
    }).format(amount);
  }

  formatDate(date: Date | undefined): string {
    if (!date) return '';
    return new Date(date).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  }

  getCaseStatusBadge(caseItem: Case): string {
    if (caseItem.courtCaseNumber) {
      return 'badge bg-success';
    }
    return 'badge bg-warning text-dark';
  }

  getCaseStatusText(caseItem: Case): string {
    if (caseItem.courtCaseNumber) {
      return 'Assigned';
    }
    return 'Unassigned';
  }
}
