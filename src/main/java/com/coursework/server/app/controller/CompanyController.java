package com.coursework.server.app.controller;

import com.coursework.server.app.model.Company;
import com.coursework.server.app.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin // Если клиент будет запускаться отдельно
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/user/{userId}")
    public List<Company> getCompaniesByUserId(@PathVariable Long userId) {
        return companyService.getCompaniesByUserId(userId);
    }

    @PostMapping
    public Company createCompany(@RequestBody Company company) {
        return companyService.createCompany(company);
    }
}
