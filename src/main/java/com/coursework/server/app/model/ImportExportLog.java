package com.coursework.server.app.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "import_export_log")
public class ImportExportLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "action_type", nullable = false)
    private String actionType;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "timestamp", columnDefinition = "timestamp default CURRENT_TIMESTAMP")
    private LocalDateTime timestamp;

    public ImportExportLog() {
    }

    public ImportExportLog(Integer id, User user, String actionType, String fileName, LocalDateTime timestamp) {
        this.id = id;
        this.user = user;
        this.actionType = actionType;
        this.fileName = fileName;
        this.timestamp = timestamp;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

