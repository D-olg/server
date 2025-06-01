package com.coursework.server.app.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ValuationDTO {
    private BigDecimal valuation;
    private String recommendation;
    private LocalDateTime createdAt;
    private ScenarioDTO scenario;

    public ValuationDTO() {
    }

    public ValuationDTO(BigDecimal valuation, String recommendation, LocalDateTime createdAt, ScenarioDTO scenario) {
        this.valuation = valuation;
        this.recommendation = recommendation;
        this.createdAt = createdAt;
        this.scenario = scenario;
    }

    public BigDecimal getValuation() {
        return valuation;
    }

    public void setValuation(BigDecimal valuation) {
        this.valuation = valuation;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ScenarioDTO getScenario() {
        return scenario;
    }

    public void setScenario(ScenarioDTO scenario) {
        this.scenario = scenario;
    }
}