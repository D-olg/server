package com.coursework.server.app.controller;

import com.coursework.server.app.dto.ValuationDTO;
import com.coursework.server.app.service.ExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class ExportController {

    private final ExportService exportService;

    public ExportController(ExportService exportService) {
        this.exportService = exportService;
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> exportValuations(
            @RequestBody List<ValuationDTO> valuations,
            @RequestParam String format,
            Principal principal) throws IOException {

        byte[] fileBytes = exportService.export(valuations, format, principal);

        String fileName = "valuations." + format.toLowerCase();
        MediaType mediaType = switch (format.toUpperCase()) {
            case "CSV" -> MediaType.TEXT_PLAIN;
            case "XLSX" -> MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            case "JSON" -> MediaType.APPLICATION_JSON;
            case "PDF" -> MediaType.APPLICATION_PDF;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(mediaType)
                .body(fileBytes);
    }

}
