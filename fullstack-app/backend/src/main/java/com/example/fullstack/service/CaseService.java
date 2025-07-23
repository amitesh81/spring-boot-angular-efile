package com.example.fullstack.service;

import com.example.fullstack.entity.Case;
import com.example.fullstack.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class CaseService {
    
    @Autowired
    private CaseRepository caseRepository;
    
    public List<Case> getAllCases() {
        return caseRepository.findAll();
    }
    
    public Optional<Case> getCaseById(Long id) {
        return caseRepository.findById(id);
    }
    
    public Optional<Case> getCaseBySubmissionNumber(String submissionNumber) {
        return caseRepository.findBySubmissionNumber(submissionNumber);
    }
    
    public Optional<Case> getCaseByCourtCaseNumber(String courtCaseNumber) {
        return caseRepository.findByCourtCaseNumber(courtCaseNumber);
    }
    
    public Case createCase(Case caseEntity) {
        validateCase(caseEntity);
        return caseRepository.save(caseEntity);
    }
    
    public Case updateCase(Long id, Case caseDetails) {
        Case existingCase = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found with id: " + id));
        
        // Check if court case number is being changed and if new number already exists
        if (caseDetails.getCourtCaseNumber() != null && 
            !caseDetails.getCourtCaseNumber().equals(existingCase.getCourtCaseNumber()) &&
            caseRepository.existsByCourtCaseNumber(caseDetails.getCourtCaseNumber())) {
            throw new RuntimeException("Case with court case number " + caseDetails.getCourtCaseNumber() + " already exists");
        }
        
        // Update fields
        existingCase.setCourtCaseNumber(caseDetails.getCourtCaseNumber());
        existingCase.setCourtLocation(caseDetails.getCourtLocation());
        existingCase.setCaseCategory(caseDetails.getCaseCategory());
        existingCase.setCaseType(caseDetails.getCaseType());
        existingCase.setFilerReferenceNumber(caseDetails.getFilerReferenceNumber());
        existingCase.setFilingFee(caseDetails.getFilingFee());
        existingCase.setFilingAmount(caseDetails.getFilingAmount());
        existingCase.setExempt(caseDetails.getExempt());
        existingCase.setGovAttorney(caseDetails.getGovAttorney());
        existingCase.setInformaPauperis(caseDetails.getInformaPauperis());
        existingCase.setWaived(caseDetails.getWaived());
        existingCase.setNoteToClerk(caseDetails.getNoteToClerk());
        
        validateCase(existingCase);
        return caseRepository.save(existingCase);
    }
    
    public void deleteCase(Long id) {
        Case caseEntity = caseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Case not found with id: " + id));
        caseRepository.delete(caseEntity);
    }
    
    public List<Case> getCasesByCourtLocation(String courtLocation) {
        return caseRepository.findByCourtLocation(courtLocation);
    }
    
    public List<Case> getCasesByCategory(String caseCategory) {
        return caseRepository.findByCaseCategory(caseCategory);
    }
    
    public List<Case> getCasesByType(String caseType) {
        return caseRepository.findByCaseType(caseType);
    }
    
    public List<Case> getUnassignedCases() {
        return caseRepository.findUnassignedCases();
    }
    
    public List<Case> getAssignedCases() {
        return caseRepository.findAssignedCases();
    }
    
    public List<Case> getCasesWithFeeExemptions() {
        return caseRepository.findCasesWithFeeExemptions();
    }
    
    public List<Case> getCasesWithFilingFees() {
        return caseRepository.findCasesWithFilingFees();
    }
    
    public List<Case> searchByFilerReference(String filerReferenceNumber) {
        return caseRepository.findByFilerReferenceNumber(filerReferenceNumber);
    }
    
    public List<Case> searchByNoteKeyword(String keyword) {
        return caseRepository.findByNoteToClerkContaining(keyword);
    }
    
    public BigDecimal calculateFilingFee(String caseType) {
        // Mock fee calculation based on case type
        switch (caseType) {
            case "CIVIL_GENERAL":
            case "CIVIL_CONTRACT":
            case "CIVIL_TORT":
                return new BigDecimal("165.00");
            case "CIVIL_SMALL_CLAIMS":
                return new BigDecimal("35.00");
            case "FAM_DIVORCE":
            case "FAM_CUSTODY":
            case "FAM_SUPPORT":
            case "FAM_ADOPTION":
                return new BigDecimal("163.00");
            case "PROB_ESTATE":
            case "PROB_GUARDIANSHIP":
            case "PROB_CONSERVATORSHIP":
                return new BigDecimal("163.00");
            case "MUN_ORDINANCE":
            case "MUN_CODE":
                return new BigDecimal("50.00");
            case "CRIM_FELONY":
            case "CRIM_MISDEMEANOR":
            case "CRIM_TRAFFIC":
            case "JUV_DELINQUENCY":
            case "JUV_ABUSE_NEGLECT":
                return BigDecimal.ZERO; // Criminal and juvenile cases typically have no filing fee
            default:
                return new BigDecimal("100.00"); // Default fee
        }
    }
    
    private void validateCase(Case caseEntity) {
        // Validate required fields
        if (caseEntity.getCourtLocation() == null || caseEntity.getCourtLocation().trim().isEmpty()) {
            throw new RuntimeException("Court location is required");
        }
        
        if (caseEntity.getCaseCategory() == null || caseEntity.getCaseCategory().trim().isEmpty()) {
            throw new RuntimeException("Case category is required");
        }
        
        if (caseEntity.getCaseType() == null || caseEntity.getCaseType().trim().isEmpty()) {
            throw new RuntimeException("Case type is required");
        }
        
        // Validate filing fee logic
        boolean hasExemption = Boolean.TRUE.equals(caseEntity.getExempt()) ||
                              Boolean.TRUE.equals(caseEntity.getGovAttorney()) ||
                              Boolean.TRUE.equals(caseEntity.getInformaPauperis()) ||
                              Boolean.TRUE.equals(caseEntity.getWaived());
        
        boolean hasFee = caseEntity.getFilingAmount() != null && 
                        caseEntity.getFilingAmount().compareTo(BigDecimal.ZERO) > 0;
        
        // For case types that require fees, must have either fee or exemption
        if (requiresFilingFee(caseEntity.getCaseType()) && !hasExemption && !hasFee) {
            throw new RuntimeException("Filing fee or exemption is required for this case type");
        }
        
        // Validate filer reference number length
        if (caseEntity.getFilerReferenceNumber() != null && 
            caseEntity.getFilerReferenceNumber().length() > 30) {
            throw new RuntimeException("Filer reference number cannot exceed 30 characters");
        }
        
        // Validate note to clerk length
        if (caseEntity.getNoteToClerk() != null && 
            caseEntity.getNoteToClerk().length() > 1000) {
            throw new RuntimeException("Note to clerk cannot exceed 1000 characters");
        }
    }
    
    private boolean requiresFilingFee(String caseType) {
        // Criminal and juvenile cases typically don't require filing fees
        return !caseType.startsWith("CRIM_") && !caseType.startsWith("JUV_");
    }
}
