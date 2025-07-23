package com.example.fullstack.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cases")
public class Case {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String submissionNumber;
    
    private String courtCaseNumber;
    
    @NotBlank(message = "Court location is required")
    @Column(nullable = false)
    private String courtLocation;
    
    @NotBlank(message = "Case category is required")
    @Column(nullable = false)
    private String caseCategory;
    
    @NotBlank(message = "Case type is required")
    @Column(nullable = false)
    private String caseType;
    
    @Size(max = 30, message = "Filer reference number cannot exceed 30 characters")
    private String filerReferenceNumber;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Filing fee must be positive")
    @Digits(integer = 10, fraction = 2, message = "Filing fee must be a valid monetary amount")
    private BigDecimal filingFee;
    
    @DecimalMin(value = "0.0", inclusive = true, message = "Filing amount must be positive")
    @Digits(integer = 10, fraction = 2, message = "Filing amount must be a valid monetary amount")
    private BigDecimal filingAmount;
    
    @Column(nullable = false)
    private Boolean exempt = false;
    
    @Column(nullable = false)
    private Boolean govAttorney = false;
    
    @Column(nullable = false)
    private Boolean informaPauperis = false;
    
    @Column(nullable = false)
    private Boolean waived = false;
    
    @Size(max = 1000, message = "Note to clerk cannot exceed 1000 characters")
    @Column(length = 1000)
    private String noteToClerk;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    
    @Column(nullable = false)
    private LocalDateTime updatedDate;
    
    // Constructors
    public Case() {}
    
    public Case(String courtLocation, String caseCategory, String caseType) {
        this.courtLocation = courtLocation;
        this.caseCategory = caseCategory;
        this.caseType = caseType;
        this.exempt = false;
        this.govAttorney = false;
        this.informaPauperis = false;
        this.waived = false;
    }
    
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdDate = now;
        this.updatedDate = now;
        if (this.submissionNumber == null) {
            this.submissionNumber = generateSubmissionNumber();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
    
    private String generateSubmissionNumber() {
        return "SUB-" + System.currentTimeMillis();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getSubmissionNumber() {
        return submissionNumber;
    }
    
    public void setSubmissionNumber(String submissionNumber) {
        this.submissionNumber = submissionNumber;
    }
    
    public String getCourtCaseNumber() {
        return courtCaseNumber;
    }
    
    public void setCourtCaseNumber(String courtCaseNumber) {
        this.courtCaseNumber = courtCaseNumber;
    }
    
    public String getCourtLocation() {
        return courtLocation;
    }
    
    public void setCourtLocation(String courtLocation) {
        this.courtLocation = courtLocation;
    }
    
    public String getCaseCategory() {
        return caseCategory;
    }
    
    public void setCaseCategory(String caseCategory) {
        this.caseCategory = caseCategory;
    }
    
    public String getCaseType() {
        return caseType;
    }
    
    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }
    
    public String getFilerReferenceNumber() {
        return filerReferenceNumber;
    }
    
    public void setFilerReferenceNumber(String filerReferenceNumber) {
        this.filerReferenceNumber = filerReferenceNumber;
    }
    
    public BigDecimal getFilingFee() {
        return filingFee;
    }
    
    public void setFilingFee(BigDecimal filingFee) {
        this.filingFee = filingFee;
    }
    
    public BigDecimal getFilingAmount() {
        return filingAmount;
    }
    
    public void setFilingAmount(BigDecimal filingAmount) {
        this.filingAmount = filingAmount;
    }
    
    public Boolean getExempt() {
        return exempt;
    }
    
    public void setExempt(Boolean exempt) {
        this.exempt = exempt;
    }
    
    public Boolean getGovAttorney() {
        return govAttorney;
    }
    
    public void setGovAttorney(Boolean govAttorney) {
        this.govAttorney = govAttorney;
    }
    
    public Boolean getInformaPauperis() {
        return informaPauperis;
    }
    
    public void setInformaPauperis(Boolean informaPauperis) {
        this.informaPauperis = informaPauperis;
    }
    
    public Boolean getWaived() {
        return waived;
    }
    
    public void setWaived(Boolean waived) {
        this.waived = waived;
    }
    
    public String getNoteToClerk() {
        return noteToClerk;
    }
    
    public void setNoteToClerk(String noteToClerk) {
        this.noteToClerk = noteToClerk;
    }
    
    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
    
    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }
    
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
    
    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    // Utility methods
    public boolean hasAnyExemption() {
        return Boolean.TRUE.equals(exempt) || 
               Boolean.TRUE.equals(govAttorney) || 
               Boolean.TRUE.equals(informaPauperis) || 
               Boolean.TRUE.equals(waived);
    }
    
    public boolean isAssigned() {
        return courtCaseNumber != null && !courtCaseNumber.trim().isEmpty();
    }
}
