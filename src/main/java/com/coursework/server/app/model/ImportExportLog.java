package com.coursework.server.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "import_export_log")
public class ImportExportLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "action_type")
    private String actionType;

    @Column(name = "file_name")
    private String fileName;

    private LocalDateTime timestamp;

    // Геттеры и сеттеры
}
