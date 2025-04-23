package com.coursework.server.app.repository;

import com.coursework.server.app.model.DiscountRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiscountRateRepository extends JpaRepository<DiscountRate, Long> {
    List<DiscountRate> findByCompanyId(Long companyId);
}
