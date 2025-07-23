package com.example.fullstack.controller;

import com.example.fullstack.entity.Case;
import com.example.fullstack.service.CaseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/api/cases")
@CrossOrigin(origins = "http://localhost:4200")
public class CaseController {
    
    @Autowired
    private CaseService caseService;
    
    @GetMapping
    public ResponseEntity<List<Case>> getAllCases() {
        List<Case> cases = caseService.getAllCases();
        return ResponseEntity.ok(cases);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Case> getCaseById(@PathVariable Long id) {
        Optional<Case> caseEntity = caseService.getCaseById(id);
        return caseEntity.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/submission/{submissionNumber}")
    public ResponseEntity<Case> getCaseBySubmissionNumber(@PathVariable String submissionNumber) {
        Optional<Case> caseEntity = caseService.getCaseBySubmissionNumber(submissionNumber);
        return caseEntity.map(ResponseEntity::ok)
                         .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> createCase(@Valid @RequestBody Case caseEntity) {
        try {
            Case createdCase = caseService.createCase(caseEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCase(@PathVariable Long id, @Valid @RequestBody Case caseDetails) {
        try {
            Case updatedCase = caseService.updateCase(id, caseDetails);
            return ResponseEntity.ok(updatedCase);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCase(@PathVariable Long id) {
        try {
            caseService.deleteCase(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // Court locations endpoint
    @GetMapping("/court-locations")
    public ResponseEntity<List<Map<String, String>>> getCourtLocations() {
        List<Map<String, String>> locations = Arrays.asList(
            createOption("STL_CITY", "St. Louis City Circuit Court"),
            createOption("STL_COUNTY", "St. Louis County Circuit Court"),
            createOption("JACKSON", "Jackson County Circuit Court"),
            createOption("GREENE", "Greene County Circuit Court"),
            createOption("CLAY", "Clay County Circuit Court"),
            createOption("JEFFERSON", "Jefferson County Circuit Court"),
            createOption("BOONE", "Boone County Circuit Court"),
            createOption("COLE", "Cole County Circuit Court"),
            createOption("FRANKLIN", "Franklin County Circuit Court"),
            createOption("CASS", "Cass County Circuit Court")
        );
        return ResponseEntity.ok(locations);
    }
    
    // Case categories endpoint
    @GetMapping("/categories")
    public ResponseEntity<List<Map<String, String>>> getCaseCategories() {
        List<Map<String, String>> categories = Arrays.asList(
            createOption("CIVIL", "Civil"),
            createOption("CRIMINAL", "Criminal"),
            createOption("FAMILY", "Family"),
            createOption("PROBATE", "Probate"),
            createOption("JUVENILE", "Juvenile"),
            createOption("MUNICIPAL", "Municipal")
        );
        return ResponseEntity.ok(categories);
    }
    
    // Case types by category endpoint
    @GetMapping("/types/{categoryId}")
    public ResponseEntity<List<Map<String, Object>>> getCaseTypesByCategory(@PathVariable String categoryId) {
        List<Map<String, Object>> caseTypes = new ArrayList<>();
        
        switch (categoryId) {
            case "CIVIL":
                caseTypes.addAll(Arrays.asList(
                    createCaseTypeOption("CIVIL_GENERAL", "General Civil", categoryId, true, new BigDecimal("165.00")),
                    createCaseTypeOption("CIVIL_CONTRACT", "Contract Dispute", categoryId, true, new BigDecimal("165.00")),
                    createCaseTypeOption("CIVIL_TORT", "Tort", categoryId, true, new BigDecimal("165.00")),
                    createCaseTypeOption("CIVIL_SMALL_CLAIMS", "Small Claims", categoryId, true, new BigDecimal("35.00"))
                ));
                break;
            case "CRIMINAL":
                caseTypes.addAll(Arrays.asList(
                    createCaseTypeOption("CRIM_FELONY", "Felony", categoryId, false, BigDecimal.ZERO),
                    createCaseTypeOption("CRIM_MISDEMEANOR", "Misdemeanor", categoryId, false, BigDecimal.ZERO),
                    createCaseTypeOption("CRIM_TRAFFIC", "Traffic", categoryId, false, BigDecimal.ZERO)
                ));
                break;
            case "FAMILY":
                caseTypes.addAll(Arrays.asList(
                    createCaseTypeOption("FAM_DIVORCE", "Divorce", categoryId, true, new BigDecimal("163.00")),
                    createCaseTypeOption("FAM_CUSTODY", "Child Custody", categoryId, true, new BigDecimal("163.00")),
                    createCaseTypeOption("FAM_SUPPORT", "Child Support", categoryId, true, new BigDecimal("163.00")),
                    createCaseTypeOption("FAM_ADOPTION", "Adoption", categoryId, true, new BigDecimal("163.00"))
                ));
                break;
            case "PROBATE":
                caseTypes.addAll(Arrays.asList(
                    createCaseTypeOption("PROB_ESTATE", "Estate Administration", categoryId, true, new BigDecimal("163.00")),
                    createCaseTypeOption("PROB_GUARDIANSHIP", "Guardianship", categoryId, true, new BigDecimal("163.00")),
                    createCaseTypeOption("PROB_CONSERVATORSHIP", "Conservatorship", categoryId, true, new BigDecimal("163.00"))
                ));
                break;
            case "JUVENILE":
                caseTypes.addAll(Arrays.asList(
                    createCaseTypeOption("JUV_DELINQUENCY", "Juvenile Delinquency", categoryId, false, BigDecimal.ZERO),
                    createCaseTypeOption("JUV_ABUSE_NEGLECT", "Abuse and Neglect", categoryId, false, BigDecimal.ZERO)
                ));
                break;
            case "MUNICIPAL":
                caseTypes.addAll(Arrays.asList(
                    createCaseTypeOption("MUN_ORDINANCE", "Ordinance Violation", categoryId, true, new BigDecimal("50.00")),
                    createCaseTypeOption("MUN_CODE", "Code Enforcement", categoryId, true, new BigDecimal("50.00"))
                ));
                break;
        }
        
        return ResponseEntity.ok(caseTypes);
    }
    
    // Filing fee calculation endpoint
    @GetMapping("/filing-fee/{caseType}")
    public ResponseEntity<Map<String, BigDecimal>> calculateFilingFee(@PathVariable String caseType) {
        BigDecimal fee = caseService.calculateFilingFee(caseType);
        Map<String, BigDecimal> response = new HashMap<>();
        response.put("fee", fee);
        return ResponseEntity.ok(response);
    }
    
    // Search endpoints
    @GetMapping("/search/court-location/{courtLocation}")
    public ResponseEntity<List<Case>> getCasesByCourtLocation(@PathVariable String courtLocation) {
        List<Case> cases = caseService.getCasesByCourtLocation(courtLocation);
        return ResponseEntity.ok(cases);
    }
    
    @GetMapping("/search/category/{caseCategory}")
    public ResponseEntity<List<Case>> getCasesByCategory(@PathVariable String caseCategory) {
        List<Case> cases = caseService.getCasesByCategory(caseCategory);
        return ResponseEntity.ok(cases);
    }
    
    @GetMapping("/search/type/{caseType}")
    public ResponseEntity<List<Case>> getCasesByType(@PathVariable String caseType) {
        List<Case> cases = caseService.getCasesByType(caseType);
        return ResponseEntity.ok(cases);
    }
    
    @GetMapping("/search/unassigned")
    public ResponseEntity<List<Case>> getUnassignedCases() {
        List<Case> cases = caseService.getUnassignedCases();
        return ResponseEntity.ok(cases);
    }
    
    @GetMapping("/search/assigned")
    public ResponseEntity<List<Case>> getAssignedCases() {
        List<Case> cases = caseService.getAssignedCases();
        return ResponseEntity.ok(cases);
    }
    
    @GetMapping("/search/exemptions")
    public ResponseEntity<List<Case>> getCasesWithFeeExemptions() {
        List<Case> cases = caseService.getCasesWithFeeExemptions();
        return ResponseEntity.ok(cases);
    }
    
    @GetMapping("/search/filing-fees")
    public ResponseEntity<List<Case>> getCasesWithFilingFees() {
        List<Case> cases = caseService.getCasesWithFilingFees();
        return ResponseEntity.ok(cases);
    }
    
    @GetMapping("/search/filer-reference/{filerReferenceNumber}")
    public ResponseEntity<List<Case>> searchByFilerReference(@PathVariable String filerReferenceNumber) {
        List<Case> cases = caseService.searchByFilerReference(filerReferenceNumber);
        return ResponseEntity.ok(cases);
    }
    
    @GetMapping("/search/notes")
    public ResponseEntity<List<Case>> searchByNoteKeyword(@RequestParam String keyword) {
        List<Case> cases = caseService.searchByNoteKeyword(keyword);
        return ResponseEntity.ok(cases);
    }
    
    // Helper methods
    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }
    
    private Map<String, Object> createCaseTypeOption(String value, String label, String categoryId, 
                                                   boolean requiresFee, BigDecimal defaultFee) {
        Map<String, Object> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        option.put("categoryId", categoryId);
        option.put("requiresFee", requiresFee);
        option.put("defaultFee", defaultFee);
        return option;
    }
}
