package com.coursework.server.app.controller;

import com.coursework.server.app.service.ImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
public class ImportController {

    private final ImportService importService;

    public ImportController(ImportService importService) {
        this.importService = importService;
    }

    @PostMapping("/import")
    public ResponseEntity<String> importFinancialData(
            @RequestParam("file") MultipartFile file,
            @RequestParam("format") String format,
            Principal principal) {

        try {
            importService.importAndCalculate(file, principal);
            return ResponseEntity.ok("Импорт и расчет завершены успешно.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Ошибка в данных: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Ошибка при импорте: " + e.getMessage());
        }
    }
}
