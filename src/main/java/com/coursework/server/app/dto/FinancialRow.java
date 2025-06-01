package com.coursework.server.app.dto;

import java.math.BigDecimal;

public class FinancialRow {
    private int year;
    private BigDecimal equity;
    private BigDecimal netIncome;
    private BigDecimal discountRate;

    public FinancialRow(int year, BigDecimal equity, BigDecimal netIncome, BigDecimal discountRate) {
        this.year = year;
        this.equity = equity;
        this.netIncome = netIncome;
        this.discountRate = discountRate;
    }

    public FinancialRow() {

    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public BigDecimal getEquity() {
        return equity;
    }

    public void setEquity(BigDecimal equity) {
        this.equity = equity;
    }

    public BigDecimal getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(BigDecimal netIncome) {
        this.netIncome = netIncome;
    }

    public BigDecimal getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(BigDecimal discountRate) {
        this.discountRate = discountRate;
    }
}
