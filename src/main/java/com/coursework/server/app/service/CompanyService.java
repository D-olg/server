package com.coursework.server.app.service;

import com.coursework.server.app.model.Company;
import com.coursework.server.app.model.User;
import com.coursework.server.app.repository.CompanyRepository;
import com.coursework.server.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserRepository userRepository;

    public Company getCompanyByUserId(int userId) {
        return companyRepository.findByUser_Id(userId);
    }

    public boolean updateUserCompany(Company company) {
        if (!companyRepository.existsById(company.getId())) {
            return false;
        }
        
        if (company.getUserId() != null) {
            User user = userRepository.findById(company.getUserId().longValue()).orElse(null);
            if (user != null) {
                company.setUser(user);
            }
        }
        
        companyRepository.save(company);
        return true;
    }

    public List<Company> getAllCompanies() {
        return companyRepository.findAll();
    }

    public void saveCompany(Company company) {
        if (company.getId() != null && company.getId() == 0) {
            company.setId(null);
        }
        if (company.getUserId() != null) {
            User user = userRepository.findById(company.getUserId().longValue()).orElse(null);
            if (user != null) {
                company.setUser(user);
            }
        }
        
        if (company.getId() == null) {
            companyRepository.saveAndFlush(company);
        } else {
            Company existingCompany = companyRepository.findById(company.getId()).orElse(null);
            if (existingCompany != null) {
                existingCompany.setName(company.getName());
                existingCompany.setDescription(company.getDescription());
                existingCompany.setUser(company.getUser());
                existingCompany.setCreatedAt(company.getCreatedAt());
                companyRepository.save(existingCompany);
            }
        }
    }

    public boolean updateCompany(Integer id, Company updatedCompany) {
        return companyRepository.findById(id).map(existingCompany -> {
            existingCompany.setName(updatedCompany.getName());
            existingCompany.setDescription(updatedCompany.getDescription());
            existingCompany.setCreatedAt(updatedCompany.getCreatedAt());
            
            if (updatedCompany.getUserId() != null) {
                User user = userRepository.findById(updatedCompany.getUserId().longValue()).orElse(null);
                if (user != null) {
                    existingCompany.setUser(user);
                }
            }
            
            companyRepository.save(existingCompany);
            return true;
        }).orElse(false);
    }

    public boolean deleteCompany(Integer id) {
        if (companyRepository.existsById(id)) {
            companyRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}