package com.coursework.server.app.repository;

import com.coursework.server.app.model.Valuation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ValuationRepository extends JpaRepository<Valuation, Long> {
    List<Valuation> findByCompanyId(Long companyId);
    List<Valuation> findByUserId(Long userId);
}
