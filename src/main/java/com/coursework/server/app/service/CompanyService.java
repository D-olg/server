package com.coursework.server.app.service;

import com.coursework.server.app.model.Company;
import com.coursework.server.app.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    @Autowired
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public List<Company> getCompaniesByUserId(Long userId) {
        return companyRepository.findByUserId(userId);
    }
}
