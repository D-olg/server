package com.coursework.server.app.controller;

import com.coursework.server.app.model.Company;
import com.coursework.server.app.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminCompanyController {

    private final CompanyService companyService;

    @Autowired
    public AdminCompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    public ResponseEntity<List<Company>> getAllCompanies() {
        System.out.println("Запрос на получение всех компаний для админа...");
        try {
            List<Company> companies = companyService.getAllCompanies();
            return ResponseEntity.ok(companies);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/companies")
    public ResponseEntity<String> addCompany(@RequestBody Company company) {
        try {
            company.setCreatedAt(LocalDateTime.now());
            companyService.saveCompany(company);
            return ResponseEntity.ok("Company added successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to add company.");
        }
    }

    @PutMapping("/companies/{id}")
    public ResponseEntity<String> updateCompany(@PathVariable Integer id, @RequestBody Company updatedCompany) {
        try {
            boolean success = companyService.updateCompany(id, updatedCompany);
            if (success) {
                return ResponseEntity.ok("Company updated successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to update company.");
        }
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<String> deleteCompany(@PathVariable Integer id) {
        try {
            boolean success = companyService.deleteCompany(id);
            if (success) {
                return ResponseEntity.ok("Company deleted successfully.");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to delete company.");
        }
    }
}
