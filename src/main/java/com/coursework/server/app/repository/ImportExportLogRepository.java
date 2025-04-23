package com.coursework.server.app.repository;

import com.coursework.server.app.model.ImportExportLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImportExportLogRepository extends JpaRepository<ImportExportLog, Long> {
    List<ImportExportLog> findByUserId(Long userId);
}
