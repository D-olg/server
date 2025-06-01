package com.coursework.server.app.repository;

import com.coursework.server.app.model.Company;
import com.coursework.server.app.model.DiscountRate;
import com.coursework.server.app.model.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DiscountRateRepository extends JpaRepository<DiscountRate, Integer> {
    List<DiscountRate> findByCompanyAndScenario(Company company, Scenario scenario);
    List<DiscountRate> findAllByScenarioId(Integer scenarioId);

}
