package com.example.fullstack.repository;

import com.example.fullstack.entity.Case;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CaseRepository extends JpaRepository<Case, Long> {
    
    Optional<Case> findBySubmissionNumber(String submissionNumber);
    
    Optional<Case> findByCourtCaseNumber(String courtCaseNumber);
    
    List<Case> findByCourtLocation(String courtLocation);
    
    List<Case> findByCaseCategory(String caseCategory);
    
    List<Case> findByCaseType(String caseType);
    
    List<Case> findByCourtLocationAndCaseCategory(String courtLocation, String caseCategory);
    
    @Query("SELECT c FROM Case c WHERE c.courtCaseNumber IS NULL OR c.courtCaseNumber = ''")
    List<Case> findUnassignedCases();
    
    @Query("SELECT c FROM Case c WHERE c.courtCaseNumber IS NOT NULL AND c.courtCaseNumber != ''")
    List<Case> findAssignedCases();
    
    @Query("SELECT c FROM Case c WHERE c.exempt = true OR c.govAttorney = true OR c.informaPauperis = true OR c.waived = true")
    List<Case> findCasesWithFeeExemptions();
    
    @Query("SELECT c FROM Case c WHERE c.filingFee > 0 AND (c.exempt = false AND c.govAttorney = false AND c.informaPauperis = false AND c.waived = false)")
    List<Case> findCasesWithFilingFees();
    
    @Query("SELECT c FROM Case c WHERE c.filerReferenceNumber = :refNumber")
    List<Case> findByFilerReferenceNumber(@Param("refNumber") String filerReferenceNumber);
    
    @Query("SELECT c FROM Case c WHERE LOWER(c.noteToClerk) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Case> findByNoteToClerkContaining(@Param("keyword") String keyword);
    
    boolean existsBySubmissionNumber(String submissionNumber);
    
    boolean existsByCourtCaseNumber(String courtCaseNumber);
}
