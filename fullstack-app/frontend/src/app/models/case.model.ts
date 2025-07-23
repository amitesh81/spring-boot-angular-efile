export interface Case {
  id?: number;
  submissionNumber?: string;
  courtCaseNumber?: string;
  courtLocation: string;
  caseCategory: string;
  caseType: string;
  filerReferenceNumber?: string;
  filingFee?: number;
  filingAmount?: number;
  exempt?: boolean;
  govAttorney?: boolean;
  informaPauperis?: boolean;
  waived?: boolean;
  noteToClerk?: string;
  createdDate?: Date;
  updatedDate?: Date;
}

export interface CaseFormModel {
  courtLocation: string;
  caseCategory: string;
  caseType: string;
  filerReferenceNo: string;
  filingAmount: number;
  exempt: boolean;
  govAttorney: boolean;
  informaPauperis: boolean;
  waived: boolean;
  noteToClerk: string;
}

export interface CourtLocation {
  value: string;
  label: string;
}

export interface CaseCategory {
  value: string;
  label: string;
}

export interface CaseType {
  value: string;
  label: string;
  categoryId: string;
  requiresFee: boolean;
  autoCalculateFee?: boolean;
  defaultFee?: number;
}

export interface DropdownOption {
  value: string;
  label: string;
}
