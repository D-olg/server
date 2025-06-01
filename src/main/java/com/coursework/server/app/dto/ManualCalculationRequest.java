package com.coursework.server.app.dto;

import java.math.BigDecimal;
import java.util.List;

public class ManualCalculationRequest  {
    private String description;
    private List<FinancialRow> data;

    public ManualCalculationRequest(String description, List<FinancialRow> data) {
        this.description = description;
        this.data = data;
    }

    public ManualCalculationRequest() {

    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<FinancialRow> getData() {
        return data;
    }

    public void setData(List<FinancialRow> data) {
        this.data = data;
    }
}
