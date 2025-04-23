package com.coursework.server.app.model;

import jakarta.persistence.*;

@Entity
@Table(name = "discount_rate")
public class DiscountRate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    private Integer year;
    private Double rate;

    // Геттеры и сеттеры
}
