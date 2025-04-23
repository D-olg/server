package com.coursework.server.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "financial_data")
public class FinancialData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private Integer year;
    private Double equity;
    private Double netIncome;

    // Геттеры и сеттеры
}
