package com.coursework.server.app.repository;

import com.coursework.server.app.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    Company findByUser_Id(Integer userId);
}
