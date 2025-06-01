package com.coursework.server.app.repository;

import com.coursework.server.app.model.Company;
import com.coursework.server.app.model.FinancialData;
import com.coursework.server.app.model.Scenario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FinancialDataRepository extends JpaRepository<FinancialData, Integer> {
    List<FinancialData> findByCompanyAndScenario(Company company, Scenario scenario);
    List<FinancialData> findAllByScenarioId(Integer scenarioId);

}
