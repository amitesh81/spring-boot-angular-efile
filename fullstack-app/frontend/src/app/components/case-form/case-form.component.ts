import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CaseService } from '../../services/case.service';
import { Case, CaseFormModel, CourtLocation, CaseCategory, CaseType } from '../../models/case.model';

@Component({
  selector: 'app-case-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './case-form.component.html',
  styleUrls: ['./case-form.component.css']
})
export class CaseFormComponent implements OnInit {
  @Input() caseData: Case | null = null;
  @Input() isEditMode: boolean = false;
  @Output() formSubmit = new EventEmitter<CaseFormModel>();
  @Output() formCancel = new EventEmitter<void>();

  caseForm: FormGroup;
  courtLocations: CourtLocation[] = [];
  caseCategories: CaseCategory[] = [];
  caseTypes: CaseType[] = [];
  selectedCaseType: CaseType | null = null;
  
  showFilingFeeSection = false;
  showAutoFeeMessage = false;
  calculatedFee = 0;
  loading = false;
  error = '';

  constructor(
    private fb: FormBuilder,
    private caseService: CaseService
  ) {
    this.caseForm = this.createForm();
  }

  ngOnInit() {
    this.loadDropdownData();
    this.setupFormSubscriptions();
    
    if (this.caseData && this.isEditMode) {
      this.populateForm();
    }
  }

  private createForm(): FormGroup {
    return this.fb.group({
      courtLocation: ['', Validators.required],
      caseCategory: ['', Validators.required],
      caseType: ['', Validators.required],
      filerReferenceNo: ['', [Validators.maxLength(30)]],
      filingAmount: [0.00, [Validators.min(0), Validators.pattern(/^\d+(\.\d{1,2})?$/)]],
      exempt: [false],
      govAttorney: [false],
      informaPauperis: [false],
      waived: [false],
      noteToClerk: ['', Validators.maxLength(1000)]
    });
  }

  private loadDropdownData() {
    this.loading = true;
    
    // Load court locations
    this.caseService.getCourtLocations().subscribe({
      next: (locations) => {
        this.courtLocations = locations;
      },
      error: (error) => {
        this.error = 'Failed to load court locations';
        console.error('Error loading court locations:', error);
      }
    });

    // Load case categories
    this.caseService.getCaseCategories().subscribe({
      next: (categories) => {
        this.caseCategories = categories;
        this.loading = false;
      },
      error: (error) => {
        this.error = 'Failed to load case categories';
        this.loading = false;
        console.error('Error loading case categories:', error);
      }
    });
  }

  private setupFormSubscriptions() {
    // Watch for case category changes to update case types
    this.caseForm.get('caseCategory')?.valueChanges.subscribe(categoryId => {
      if (categoryId) {
        this.loadCaseTypes(categoryId);
        // Reset case type when category changes
        this.caseForm.patchValue({ caseType: '' });
        this.selectedCaseType = null;
        this.showFilingFeeSection = false;
        this.showAutoFeeMessage = false;
      } else {
        this.caseTypes = [];
      }
    });

    // Watch for case type changes to handle fee calculation
    this.caseForm.get('caseType')?.valueChanges.subscribe(caseTypeId => {
      if (caseTypeId) {
        this.handleCaseTypeChange(caseTypeId);
      }
    });

    // Watch for fee exemption changes
    const exemptionControls = ['exempt', 'govAttorney', 'informaPauperis', 'waived'];
    exemptionControls.forEach(controlName => {
      this.caseForm.get(controlName)?.valueChanges.subscribe(() => {
        this.validateFeeExemptions();
      });
    });
  }

  private loadCaseTypes(categoryId: string) {
    this.caseService.getCaseTypesByCategory(categoryId).subscribe({
      next: (types) => {
        this.caseTypes = types;
      },
      error: (error) => {
        this.error = 'Failed to load case types';
        console.error('Error loading case types:', error);
      }
    });
  }

  private handleCaseTypeChange(caseTypeId: string) {
    this.selectedCaseType = this.caseTypes.find(type => type.value === caseTypeId) || null;
    
    if (this.selectedCaseType) {
      this.showFilingFeeSection = this.selectedCaseType.requiresFee;
      this.showAutoFeeMessage = this.selectedCaseType.autoCalculateFee || false;
      
      if (this.selectedCaseType.defaultFee) {
        this.calculatedFee = this.selectedCaseType.defaultFee;
        this.caseForm.patchValue({ filingAmount: this.calculatedFee });
      }
    }
  }

  private validateFeeExemptions() {
    const exemptions = [
      this.caseForm.get('exempt')?.value,
      this.caseForm.get('govAttorney')?.value,
      this.caseForm.get('informaPauperis')?.value,
      this.caseForm.get('waived')?.value
    ];

    const hasExemption = exemptions.some(exemption => exemption === true);
    const filingAmount = this.caseForm.get('filingAmount')?.value;

    // Clear fee validation errors
    this.error = this.error.replace('Filing fee or exemption required.', '').trim();

    // If no exemptions and no filing amount, show error
    if (this.showFilingFeeSection && !hasExemption && (!filingAmount || filingAmount <= 0)) {
      // Will be validated on submit
    }
  }

  private populateForm() {
    if (this.caseData) {
      this.caseForm.patchValue({
        courtLocation: this.caseData.courtLocation,
        caseCategory: this.caseData.caseCategory,
        caseType: this.caseData.caseType,
        filerReferenceNo: this.caseData.filerReferenceNumber || '',
        filingAmount: this.caseData.filingAmount || 0,
        exempt: this.caseData.exempt || false,
        govAttorney: this.caseData.govAttorney || false,
        informaPauperis: this.caseData.informaPauperis || false,
        waived: this.caseData.waived || false,
        noteToClerk: this.caseData.noteToClerk || ''
      });
    }
  }

  onSubmit() {
    if (this.caseForm.valid) {
      // Additional validation for filing fees
      if (this.showFilingFeeSection) {
        const hasExemption = this.caseForm.get('exempt')?.value ||
                           this.caseForm.get('govAttorney')?.value ||
                           this.caseForm.get('informaPauperis')?.value ||
                           this.caseForm.get('waived')?.value;
        
        const filingAmount = this.caseForm.get('filingAmount')?.value;
        
        if (!hasExemption && (!filingAmount || filingAmount <= 0)) {
          this.error = 'Filing fee or exemption required.';
          return;
        }
      }

      this.error = '';
      const formData: CaseFormModel = this.caseForm.value;
      this.formSubmit.emit(formData);
    } else {
      this.markFormGroupTouched();
      this.error = 'Please correct the errors in the form.';
    }
  }

  onCancel() {
    this.formCancel.emit();
  }

  onReset() {
    this.caseForm.reset();
    this.selectedCaseType = null;
    this.showFilingFeeSection = false;
    this.showAutoFeeMessage = false;
    this.error = '';
  }

  private markFormGroupTouched() {
    Object.keys(this.caseForm.controls).forEach(key => {
      const control = this.caseForm.get(key);
      control?.markAsTouched();
    });
  }

  // Utility methods for template
  isFieldInvalid(fieldName: string): boolean {
    const field = this.caseForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  getFieldError(fieldName: string): string {
    const field = this.caseForm.get(fieldName);
    if (field && field.errors && (field.dirty || field.touched)) {
      if (field.errors['required']) return `${fieldName} is required`;
      if (field.errors['maxlength']) return `${fieldName} exceeds maximum length`;
      if (field.errors['min']) return `${fieldName} must be greater than 0`;
      if (field.errors['pattern']) return `${fieldName} format is invalid`;
    }
    return '';
  }

  onNumericInput(event: KeyboardEvent): boolean {
    // Allow only numbers and decimal point
    const charCode = event.which ? event.which : event.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57) && charCode !== 46) {
      return false;
    }
    return true;
  }
}
