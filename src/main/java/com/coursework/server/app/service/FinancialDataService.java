package com.coursework.server.app.service;

import com.coursework.server.app.model.Company;
import com.coursework.server.app.model.FinancialData;
import com.coursework.server.app.model.Scenario;
import com.coursework.server.app.repository.FinancialDataRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FinancialDataService {

    private final FinancialDataRepository financialDataRepository;

    public FinancialDataService(FinancialDataRepository financialDataRepository) {
        this.financialDataRepository = financialDataRepository;
    }

    @Transactional
    public FinancialData save(FinancialData financialData) {
        return financialDataRepository.save(financialData);
    }

    public List<FinancialData> findAllByCompanyAndScenario(Company company, Scenario scenario) {
        return financialDataRepository.findByCompanyAndScenario(company, scenario);
    }

    public List<FinancialData> getFinancialDataByScenario(Integer scenarioId) {
        return financialDataRepository.findAllByScenarioId(scenarioId);
    }

    // Other methods as needed
}
