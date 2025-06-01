package com.coursework.server.app.dto;

import java.util.List;

public class ScenarioCalculationRequest {
    private List<FinancialRow> data;
    private String description;
    private double growthRate;
    private double discountRate;
    private int durationYears;

    public ScenarioCalculationRequest(List<FinancialRow> data, String description, double growthRate, double discountRate, int durationYears) {
        this.data = data;
        this.description = description;
        this.growthRate = growthRate;
        this.discountRate = discountRate;
        this.durationYears = durationYears;
    }

    public ScenarioCalculationRequest() {

    }

    public List<FinancialRow> getData() {
        return data;
    }

    public void setData(List<FinancialRow> data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getGrowthRate() {
        return growthRate;
    }

    public void setGrowthRate(double growthRate) {
        this.growthRate = growthRate;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    public int getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(int durationYears) {
        this.durationYears = durationYears;
    }
}
