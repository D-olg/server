package com.coursework.server.app.repository;

import com.coursework.server.app.model.ImportExportLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImportExportLogRepository extends JpaRepository<ImportExportLog, Long> {
}
