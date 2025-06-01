package com.coursework.server.app.controller;

import com.coursework.server.app.dto.ScenarioDTO;
import com.coursework.server.app.dto.ValuationDTO;
import com.coursework.server.app.model.DiscountRate;
import com.coursework.server.app.model.FinancialData;
import com.coursework.server.app.model.User;
import com.coursework.server.app.model.Valuation;
import com.coursework.server.app.service.DiscountRateService;
import com.coursework.server.app.service.FinancialDataService;
import com.coursework.server.app.service.UserService;
import com.coursework.server.app.service.ValuationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user/history")
public class HistoryController {

    private final ValuationService valuationService;
    private final FinancialDataService financialDataService;
    private final DiscountRateService discountRateService;
    private final UserService userService;

    public HistoryController(ValuationService valuationService, FinancialDataService financialDataService, DiscountRateService discountRateService, UserService userService) {
        this.valuationService = valuationService;
        this.financialDataService = financialDataService;
        this.discountRateService = discountRateService;
        this.userService = userService;
    }

    @GetMapping("/valuations")
    public ResponseEntity<List<ValuationDTO>> getValuations(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Valuation> valuations = valuationService.getValuationsByUser(user.getId());

        // Преобразуем Valuation в ValuationDTO
        List<ValuationDTO> valuationDTOs = valuations.stream().map(valuation -> {
            ValuationDTO dto = new ValuationDTO();
            dto.setValuation(valuation.getValuation());
            dto.setRecommendation(valuation.getRecommendation());
            dto.setCreatedAt(valuation.getCreatedAt());

            // Преобразуем сценарий
            if (valuation.getScenario() != null) {
                ScenarioDTO scenarioDTO = new ScenarioDTO();
                scenarioDTO.setId(valuation.getScenario().getId());
                scenarioDTO.setName(valuation.getScenario().getName());
                scenarioDTO.setCompanyName(valuation.getScenario().getCompany().getName());
                dto.setScenario(scenarioDTO);
            }

            return dto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(valuationDTOs);
    }

    @GetMapping("/financial/{scenarioId}")
    public ResponseEntity<List<FinancialData>> getFinancialData(@PathVariable Integer scenarioId) {
        return ResponseEntity.ok(financialDataService.getFinancialDataByScenario(scenarioId));
    }

    @GetMapping("/rates/{scenarioId}")
    public ResponseEntity<List<DiscountRate>> getDiscountRates(@PathVariable Integer scenarioId) {
        return ResponseEntity.ok(discountRateService.getDiscountRatesByScenario(scenarioId));
    }
}
