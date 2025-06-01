package com.coursework.server.app.controller;

import com.coursework.server.app.dto.ScenarioDTO;
import com.coursework.server.app.dto.ValuationDTO;
import com.coursework.server.app.model.Company;
import com.coursework.server.app.model.Scenario;
import com.coursework.server.app.model.User;
import com.coursework.server.app.model.Valuation;
import com.coursework.server.app.service.DiscountRateService;
import com.coursework.server.app.service.FinancialDataService;
import com.coursework.server.app.service.UserService;
import com.coursework.server.app.service.ValuationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.http.ResponseEntity;

class HistoryControllerTest {

    private ValuationService valuationService;
    private FinancialDataService financialDataService;
    private DiscountRateService discountRateService;
    private UserService userService;
    private HistoryController historyController;

    @BeforeEach
    void setUp() {
        valuationService = mock(ValuationService.class);
        financialDataService = mock(FinancialDataService.class);
        discountRateService = mock(DiscountRateService.class);
        userService = mock(UserService.class);

        historyController = new HistoryController(
                valuationService,
                financialDataService,
                discountRateService,
                userService
        );
    }

    @Test
    void getValuations_returnsValuationDTOList() {
        Principal principal = () -> "testuser";

        User user = new User();
        user.setId(1);
        user.setUsername("testuser");

        Company company = new Company();
        company.setName("TestCompany");

        Scenario scenario = new Scenario();
        scenario.setId(100);
        scenario.setName("TestScenario");
        scenario.setCompany(company);

        Valuation valuation = new Valuation();
        valuation.setValuation(new BigDecimal("1234.56"));
        valuation.setRecommendation("Test recommendation");
        valuation.setCreatedAt(LocalDateTime.now());
        valuation.setScenario(scenario);

        List<Valuation> valuations = List.of(valuation);

        when(userService.findByUsername("testuser")).thenReturn(user);
        when(valuationService.getValuationsByUser(1)).thenReturn(valuations);

        ResponseEntity<List<ValuationDTO>> response = historyController.getValuations(principal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<ValuationDTO> body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());

        ValuationDTO dto = body.get(0);
        assertEquals(new BigDecimal("1234.56"), dto.getValuation());
        assertEquals("Test recommendation", dto.getRecommendation());
        assertNotNull(dto.getCreatedAt());

        ScenarioDTO scenarioDTO = dto.getScenario();
        assertNotNull(scenarioDTO);
        assertEquals(100, scenarioDTO.getId());
        assertEquals("TestScenario", scenarioDTO.getName());
        assertEquals("TestCompany", scenarioDTO.getCompanyName());

        verify(userService, times(1)).findByUsername("testuser");
        verify(valuationService, times(1)).getValuationsByUser(1);
    }
}
