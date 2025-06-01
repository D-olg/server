package com.coursework.server.app.service;

import com.coursework.server.app.model.Company;
import com.coursework.server.app.model.DiscountRate;
import com.coursework.server.app.model.Scenario;
import com.coursework.server.app.repository.DiscountRateRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscountRateService {

    private final DiscountRateRepository discountRateRepository;

    public DiscountRateService(DiscountRateRepository discountRateRepository) {
        this.discountRateRepository = discountRateRepository;
    }

    @Transactional
    public DiscountRate save(DiscountRate discountRate) {
        return discountRateRepository.save(discountRate);
    }

    public List<DiscountRate> findAllByCompanyAndScenario(Company company, Scenario scenario) {
        return discountRateRepository.findByCompanyAndScenario(company, scenario);
    }

    public List<DiscountRate> getDiscountRatesByScenario(Integer scenarioId) {
        return discountRateRepository.findAllByScenarioId(scenarioId);
    }

    // Other methods as needed
}
