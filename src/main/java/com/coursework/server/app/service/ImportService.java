package com.coursework.server.app.service;

import com.coursework.server.app.dto.FinancialRow;
import com.coursework.server.app.model.*;
import com.coursework.server.app.repository.*;
import com.opencsv.exceptions.CsvValidationException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ImportService {

    private final UserService userService;
    private final CompanyService companyService;
    private final FinancialDataRepository financialDataRepository;
    private final DiscountRateRepository discountRateRepository;
    private final ScenarioRepository scenarioRepository;
    private final ValuationService valuationService;
    private final ValuationRepository valuationRepository;
    private final ImportExportLogRepository logRepository;
    private final ImportParser importParser;

    public ImportService(
            UserService userService,
            CompanyService companyService,
            FinancialDataRepository financialDataRepository,
            DiscountRateRepository discountRateRepository,
            ScenarioRepository scenarioRepository,
            ValuationService valuationService,
            ValuationRepository valuationRepository,
            ImportExportLogRepository logRepository,
            ImportParser importParser
    ) {
        this.userService = userService;
        this.companyService = companyService;
        this.financialDataRepository = financialDataRepository;
        this.discountRateRepository = discountRateRepository;
        this.scenarioRepository = scenarioRepository;
        this.valuationService = valuationService;
        this.valuationRepository = valuationRepository;
        this.logRepository = logRepository;
        this.importParser = importParser;
    }

    @Transactional
    public void importAndCalculate(MultipartFile file, Principal principal) throws IOException, CsvValidationException {
        User user = userService.findByUsername(principal.getName());
        Company company = companyService.getCompanyByUserId(user.getId());

        List<FinancialRow> rows = importParser.parse(file);

        boolean hasForecast = rows.stream()
                .anyMatch(r -> r.getDiscountRate() == null); // Предположим: отсутствие ставок значит прогноз

        Scenario scenario = new Scenario();
        scenario.setCompany(company);
        scenario.setName("Import Scenario " + LocalDateTime.now());
        scenario.setDescription("Импорт из файла " + file.getOriginalFilename());
        scenario.setManual(!hasForecast);
        scenario.setCreatedAt(LocalDateTime.now());
        scenarioRepository.save(scenario);

        List<FinancialData> financialDataList = new java.util.ArrayList<>();
        List<DiscountRate> discountRateList = new java.util.ArrayList<>();

        for (FinancialRow row : rows) {
            FinancialData data = new FinancialData();
            data.setCompany(company);
            data.setScenario(scenario);
            data.setYear(row.getYear());
            data.setEquity(row.getEquity());
            data.setNetIncome(row.getNetIncome());
            financialDataRepository.save(data);
            financialDataList.add(data);

            if (row.getDiscountRate() != null) {
                DiscountRate rate = new DiscountRate();
                rate.setCompany(company);
                rate.setScenario(scenario);
                rate.setYear(row.getYear());
                rate.setRate(row.getDiscountRate());
                discountRateRepository.save(rate);
                discountRateList.add(rate);
            }
        }

        BigDecimal result = valuationService.calculateEboValuation(financialDataList, discountRateList);

        Valuation valuation = new Valuation();
        valuation.setCompany(company);
        valuation.setUser(user);
        valuation.setValuation(result);
        valuation.setScenario(scenario);
        valuation.setCreatedAt(LocalDateTime.now());
        valuation.setRecommendation(hasForecast ? "Расчёт с прогнозом (импорт)" : "Ручной расчёт (импорт)");
        valuationRepository.save(valuation);

        ImportExportLog log = new ImportExportLog();
        log.setUser(user);
        log.setActionType("IMPORT");
        log.setFileName(file.getOriginalFilename());
        log.setTimestamp(LocalDateTime.now());
        logRepository.save(log);

    }
}
