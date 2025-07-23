import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Case, CaseFormModel, CourtLocation, CaseCategory, CaseType } from '../models/case.model';

@Injectable({
  providedIn: 'root'
})
export class CaseService {
  private apiUrl = 'http://localhost:8080/api/cases';

  constructor(private http: HttpClient) { }

  // Case CRUD operations
  getAllCases(): Observable<Case[]> {
    return this.http.get<Case[]>(this.apiUrl);
  }

  getCaseById(id: number): Observable<Case> {
    return this.http.get<Case>(`${this.apiUrl}/${id}`);
  }

  createCase(caseData: CaseFormModel): Observable<Case> {
    return this.http.post<Case>(this.apiUrl, caseData);
  }

  updateCase(id: number, caseData: CaseFormModel): Observable<Case> {
    return this.http.put<Case>(`${this.apiUrl}/${id}`, caseData);
  }

  deleteCase(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Dropdown data services
  getCourtLocations(): Observable<CourtLocation[]> {
    // Mock data - replace with actual API call
    return of([
      { value: 'STL_CITY', label: 'St. Louis City Circuit Court' },
      { value: 'STL_COUNTY', label: 'St. Louis County Circuit Court' },
      { value: 'JACKSON', label: 'Jackson County Circuit Court' },
      { value: 'GREENE', label: 'Greene County Circuit Court' },
      { value: 'CLAY', label: 'Clay County Circuit Court' },
      { value: 'JEFFERSON', label: 'Jefferson County Circuit Court' }
    ]);
  }

  getCaseCategories(): Observable<CaseCategory[]> {
    // Mock data - replace with actual API call
    return of([
      { value: 'CIVIL', label: 'Civil' },
      { value: 'CRIMINAL', label: 'Criminal' },
      { value: 'FAMILY', label: 'Family' },
      { value: 'PROBATE', label: 'Probate' },
      { value: 'JUVENILE', label: 'Juvenile' },
      { value: 'MUNICIPAL', label: 'Municipal' }
    ]);
  }

  getCaseTypesByCategory(categoryId: string): Observable<CaseType[]> {
    // Mock data - replace with actual API call
    const caseTypes: { [key: string]: CaseType[] } = {
      'CIVIL': [
        { value: 'CIVIL_GENERAL', label: 'General Civil', categoryId: 'CIVIL', requiresFee: true, defaultFee: 165.00 },
        { value: 'CIVIL_CONTRACT', label: 'Contract Dispute', categoryId: 'CIVIL', requiresFee: true, defaultFee: 165.00 },
        { value: 'CIVIL_TORT', label: 'Tort', categoryId: 'CIVIL', requiresFee: true, defaultFee: 165.00 },
        { value: 'CIVIL_SMALL_CLAIMS', label: 'Small Claims', categoryId: 'CIVIL', requiresFee: true, defaultFee: 35.00 }
      ],
      'CRIMINAL': [
        { value: 'CRIM_FELONY', label: 'Felony', categoryId: 'CRIMINAL', requiresFee: false },
        { value: 'CRIM_MISDEMEANOR', label: 'Misdemeanor', categoryId: 'CRIMINAL', requiresFee: false },
        { value: 'CRIM_TRAFFIC', label: 'Traffic', categoryId: 'CRIMINAL', requiresFee: false }
      ],
      'FAMILY': [
        { value: 'FAM_DIVORCE', label: 'Divorce', categoryId: 'FAMILY', requiresFee: true, defaultFee: 163.00 },
        { value: 'FAM_CUSTODY', label: 'Child Custody', categoryId: 'FAMILY', requiresFee: true, defaultFee: 163.00 },
        { value: 'FAM_SUPPORT', label: 'Child Support', categoryId: 'FAMILY', requiresFee: true, defaultFee: 163.00 },
        { value: 'FAM_ADOPTION', label: 'Adoption', categoryId: 'FAMILY', requiresFee: true, defaultFee: 163.00 }
      ],
      'PROBATE': [
        { value: 'PROB_ESTATE', label: 'Estate Administration', categoryId: 'PROBATE', requiresFee: true, defaultFee: 163.00 },
        { value: 'PROB_GUARDIANSHIP', label: 'Guardianship', categoryId: 'PROBATE', requiresFee: true, defaultFee: 163.00 },
        { value: 'PROB_CONSERVATORSHIP', label: 'Conservatorship', categoryId: 'PROBATE', requiresFee: true, defaultFee: 163.00 }
      ],
      'JUVENILE': [
        { value: 'JUV_DELINQUENCY', label: 'Juvenile Delinquency', categoryId: 'JUVENILE', requiresFee: false },
        { value: 'JUV_ABUSE_NEGLECT', label: 'Abuse and Neglect', categoryId: 'JUVENILE', requiresFee: false }
      ],
      'MUNICIPAL': [
        { value: 'MUN_ORDINANCE', label: 'Ordinance Violation', categoryId: 'MUNICIPAL', requiresFee: true, defaultFee: 50.00 },
        { value: 'MUN_CODE', label: 'Code Enforcement', categoryId: 'MUNICIPAL', requiresFee: true, defaultFee: 50.00 }
      ]
    };

    return of(caseTypes[categoryId] || []);
  }

  calculateFilingFee(caseType: string): Observable<number> {
    // Mock fee calculation - replace with actual API call
    const mockFee = 165.00; // Default fee
    return of(mockFee);
  }

  validateFilerReferenceNumber(referenceNumber: string): Observable<boolean> {
    // Mock validation - replace with actual API call
    return of(referenceNumber.length <= 30);
  }
}
