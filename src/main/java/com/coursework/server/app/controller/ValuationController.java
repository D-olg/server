package com.coursework.server.app.controller;

import com.coursework.server.app.dto.FinancialRow;
import com.coursework.server.app.dto.ManualCalculationRequest;
import com.coursework.server.app.dto.ScenarioCalculationRequest;
import com.coursework.server.app.model.*;
import com.coursework.server.app.repository.*;
import com.coursework.server.app.service.CompanyService;
import com.coursework.server.app.service.UserService;
import com.coursework.server.app.service.ValuationService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/user/calculate")
public class ValuationController {

    private final ScenarioRepository scenarioRepository;
    private final FinancialDataRepository financialDataRepository;
    private final DiscountRateRepository discountRateRepository;
    private final ValuationRepository valuationRepository;
    private final CompanyService companyService;
    private final UserService userService;
    private final ValuationService valuationService;

    public ValuationController(
            ScenarioRepository scenarioRepository,
            FinancialDataRepository financialDataRepository,
            DiscountRateRepository discountRateRepository,
            ValuationRepository valuationRepository,
            CompanyService companyService,
            UserService userService,
            ValuationService valuationService
    ) {
        this.scenarioRepository = scenarioRepository;
        this.financialDataRepository = financialDataRepository;
        this.discountRateRepository = discountRateRepository;
        this.valuationRepository = valuationRepository;
        this.companyService = companyService;
        this.userService = userService;
        this.valuationService = valuationService;
    }

    @PostMapping("/manual")
    @Transactional
    public ResponseEntity<Valuation> calculateManual(@RequestBody ManualCalculationRequest request, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        Company company = companyService.getCompanyByUserId(currentUser.getId());

        System.out.println(request);
        Scenario scenario = new Scenario();
        scenario.setCompany(company);
        scenario.setName("Manual Scenario " + LocalDateTime.now());
        scenario.setDescription(request.getDescription());
        scenario.setManual(true);
        scenario.setCreatedAt(LocalDateTime.now());
        scenarioRepository.save(scenario);

        List<FinancialData> financialDataList = new ArrayList<>();
        List<DiscountRate> discountRateList = new ArrayList<>();

        for (FinancialRow row : request.getData()) {
            FinancialData data = new FinancialData();
            data.setCompany(company);
            data.setScenario(scenario);
            data.setYear(row.getYear());
            data.setEquity(row.getEquity());
            data.setNetIncome(row.getNetIncome());
            financialDataRepository.save(data);
            financialDataList.add(data);

            DiscountRate rate = new DiscountRate();
            rate.setCompany(company);
            rate.setScenario(scenario);
            rate.setYear(row.getYear());
            rate.setRate(row.getDiscountRate());
            discountRateRepository.save(rate);
            discountRateList.add(rate);
        }

        BigDecimal valuationResult = valuationService.calculateEboValuation(financialDataList, discountRateList);

        Valuation valuation = new Valuation();
        valuation.setCompany(company);
        valuation.setUser(currentUser);
        valuation.setValuation(valuationResult);
        valuation.setScenario(scenario);
        valuation.setCreatedAt(LocalDateTime.now());
        valuation.setRecommendation("Оценка выполнена вручную");
        valuationRepository.save(valuation);

        return ResponseEntity.ok(valuation);
    }

    @PostMapping("/scenario")
    @Transactional
    public ResponseEntity<Valuation> calculateScenario(@RequestBody ScenarioCalculationRequest request, Principal principal) {
        if (request.getData() == null || request.getData().isEmpty()) {
            throw new IllegalArgumentException("Данные для расчета не переданы.");
        }

        User currentUser = userService.findByUsername(principal.getName());
        Company company = companyService.getCompanyByUserId(currentUser.getId());

        Scenario scenario = new Scenario();
        scenario.setCompany(company);
        scenario.setName("Scenario " + LocalDateTime.now());
        scenario.setDescription(request.getDescription());
        scenario.setManual(false);
        scenario.setCreatedAt(LocalDateTime.now());
        scenarioRepository.save(scenario);
        System.out.println("Сценарий сохранен: " + scenario.getName());

        List<FinancialData> allFinancialData = new ArrayList<>();
        List<DiscountRate> allDiscountRates = new ArrayList<>();

        for (FinancialRow row : request.getData()) {
            FinancialData data = new FinancialData();
            data.setCompany(company);
            data.setScenario(scenario);
            data.setYear(row.getYear());
            data.setEquity(row.getEquity());
            data.setNetIncome(row.getNetIncome());
            financialDataRepository.save(data);
            allFinancialData.add(data);
            System.out.println("Фактические данные сохранены для года: " + row.getYear());

            DiscountRate rate = new DiscountRate();
            rate.setCompany(company);
            rate.setScenario(scenario);
            rate.setYear(row.getYear());
            rate.setRate(row.getDiscountRate());
            discountRateRepository.save(rate);
            allDiscountRates.add(rate);
            System.out.println("Ставка дисконтирования сохранена для года: " + row.getYear() + " со ставкой: " + row.getDiscountRate());
        }

        if (!allFinancialData.isEmpty()) {
            FinancialData lastYearData = allFinancialData.getLast();
            int lastYear = lastYearData.getYear();
            BigDecimal equity = lastYearData.getEquity();
            BigDecimal netIncome = lastYearData.getNetIncome();
            BigDecimal growth = BigDecimal.valueOf(request.getGrowthRate()).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
            BigDecimal discountRate = BigDecimal.valueOf(request.getDiscountRate());

            System.out.println("Начальные данные для прогноза: Equity = " + equity + ", NetIncome = " + netIncome + ", GrowthRate = " + growth + ", DiscountRate = " + discountRate);

            for (int i = 1; i <= request.getDurationYears(); i++) {
                int year = lastYear + i;
                equity = equity.multiply(BigDecimal.ONE.add(growth));
                netIncome = netIncome.multiply(BigDecimal.ONE.add(growth));

                System.out.println("Прогноз для года " + year + ": Equity = " + equity + ", NetIncome = " + netIncome);

                FinancialData predictedData = new FinancialData();
                predictedData.setCompany(company);
                predictedData.setScenario(scenario);
                predictedData.setYear(year);
                predictedData.setEquity(equity);
                predictedData.setNetIncome(netIncome);
                financialDataRepository.save(predictedData);
                allFinancialData.add(predictedData);

                DiscountRate predictedRate = new DiscountRate();
                predictedRate.setCompany(company);
                predictedRate.setScenario(scenario);
                predictedRate.setYear(year);
                predictedRate.setRate(discountRate);
                discountRateRepository.save(predictedRate);
                allDiscountRates.add(predictedRate);

                System.out.println("Прогнозная ставка дисконтирования для года " + year + ": " + discountRate);
            }
        }

        BigDecimal valuationResult = valuationService.calculateEboValuation(allFinancialData, allDiscountRates);
        System.out.println("Результат расчета: " + valuationResult);

        Valuation valuation = new Valuation();
        valuation.setCompany(company);
        valuation.setUser(currentUser);
        valuation.setValuation(valuationResult);
        valuation.setScenario(scenario);
        valuation.setCreatedAt(LocalDateTime.now());
        valuation.setRecommendation("Оценка с учётом прогноза");
        valuationRepository.save(valuation);
        System.out.println("Оценка сохранена: " + valuation.getValuation());

        return ResponseEntity.ok(valuation);
    }
}
