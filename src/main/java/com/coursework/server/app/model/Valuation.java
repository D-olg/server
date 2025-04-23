package com.coursework.server.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "valuation")
public class Valuation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Double valuation;
    private String recommendation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Геттеры и сеттеры
}
