package com.coursework.server.app.repository;

import com.coursework.server.app.model.FinancialData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FinancialDataRepository extends JpaRepository<FinancialData, Long> {
    List<FinancialData> findByCompanyId(Long companyId);
}
