package com.coursework.server.app.controller;

import com.coursework.server.app.dto.FinancialRow;
import com.coursework.server.app.dto.ManualCalculationRequest;
import com.coursework.server.app.model.*;
import com.coursework.server.app.repository.*;
import com.coursework.server.app.service.CompanyService;
import com.coursework.server.app.service.UserService;
import com.coursework.server.app.service.ValuationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValuationControllerTest {

    @InjectMocks
    private ValuationController valuationController;

    @Mock
    private ScenarioRepository scenarioRepository;

    @Mock
    private FinancialDataRepository financialDataRepository;

    @Mock
    private DiscountRateRepository discountRateRepository;

    @Mock
    private ValuationRepository valuationRepository;

    @Mock
    private CompanyService companyService;

    @Mock
    private UserService userService;

    @Mock
    private ValuationService valuationService;

    @Mock
    private Principal principal;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateManual() {
        // Arrange
        String username = "testuser";
        when(principal.getName()).thenReturn(username);

        User mockUser = new User();
        mockUser.setId(111);
        mockUser.setUsername(username);
        when(userService.findByUsername(username)).thenReturn(mockUser);

        Company mockCompany = new Company();
        mockCompany.setId(111);
        when(companyService.getCompanyByUserId(111)).thenReturn(mockCompany);

        FinancialRow row = new FinancialRow();
        row.setYear(2022);
        row.setEquity(BigDecimal.valueOf(1000));
        row.setNetIncome(BigDecimal.valueOf(200));
        row.setDiscountRate(BigDecimal.valueOf(0.1));

        ManualCalculationRequest request = new ManualCalculationRequest();
        request.setDescription("Test scenario");
        request.setData(List.of(row));

        when(valuationService.calculateEboValuation(anyList(), anyList()))
                .thenReturn(BigDecimal.valueOf(12345));

        ResponseEntity<Valuation> response = valuationController.calculateManual(request, principal);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(BigDecimal.valueOf(12345), response.getBody().getValuation());

        verify(scenarioRepository).save(any(Scenario.class));
        verify(financialDataRepository).save(any(FinancialData.class));
        verify(discountRateRepository).save(any(DiscountRate.class));
        verify(valuationRepository).save(any(Valuation.class));
    }
}
