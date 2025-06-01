package com.coursework.server.app.service;

import com.coursework.server.app.config.JacksonConfig;
import com.coursework.server.app.dto.ScenarioDTO;
import com.coursework.server.app.dto.ValuationDTO;
import com.coursework.server.app.model.ImportExportLog;
import com.coursework.server.app.model.User;
import com.coursework.server.app.repository.ImportExportLogRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ExportService {

    private final ImportExportLogRepository logRepository;
    private final UserService userService; // используется для получения текущего пользователя
    private final  CompanyService companyService;
    private static final ObjectMapper objectMapper = JacksonConfig.createObjectMapper();
    @Autowired
    public ExportService(ImportExportLogRepository logRepository, UserService userService, CompanyService companyService) {
        this.logRepository = logRepository;
        this.userService = userService;
        this.companyService = companyService;
    }
    // Метод для экспорта в различные форматы
    public byte[] export(List<ValuationDTO> valuations, String format, Principal principal) throws IOException {
        byte[] result;

        switch (format.toUpperCase()) {
            case "CSV":
                result = exportToCsv(valuations, principal);
                break;
            case "XLSX":
                result = exportToXlsx(valuations, principal);
                break;
            case "JSON":
                result = exportToJson(valuations, principal);
                break;
            case "PDF":
                result = exportToPdf(valuations, principal);
                break;
            default:
                throw new UnsupportedOperationException("Формат не поддерживается: " + format);
        }

        // Получаем имя файла для логирования
        String fileName = generateFileName(format);

        // Логируем экспорт
        logExport(fileName, principal);

        return result;
    }

    private String generateFileName(String format) {
        return "valuation_export_" + LocalDateTime.now().toString().replace(":", "-") + "." + format.toLowerCase();
    }

    private void logExport(String fileName, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName()); // Получаем текущего пользователя

        ImportExportLog log = new ImportExportLog();
        log.setUser(currentUser);
        log.setActionType("EXPORT");
        log.setFileName(fileName);
        log.setTimestamp(LocalDateTime.now());

        logRepository.save(log);
    }

    // Экспорт в CSV
    private byte[] exportToCsv(List<ValuationDTO> valuations, Principal principal) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Company Name,Valuation,Recommendation,Date\n");

        for (ValuationDTO valuation : valuations) {
            sb.append(valuation.getScenario().getName()).append(",")
                    .append(valuation.getValuation()).append(",")
                    .append(valuation.getRecommendation()).append(",")
                    .append(valuation.getCreatedAt()).append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    // Экспорт в Excel (XLSX)
    private byte[] exportToXlsx(List<ValuationDTO> valuations, Principal principal) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Valuations");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Company Name");
        headerRow.createCell(1).setCellValue("Valuation");
        headerRow.createCell(2).setCellValue("Recommendation");
        headerRow.createCell(3).setCellValue("Date");
        int rowNum = 1;
        for (ValuationDTO valuation : valuations) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(valuation.getScenario().getCompanyName());
            row.createCell(1).setCellValue(valuation.getValuation().toString());
            row.createCell(2).setCellValue(valuation.getRecommendation());
            row.createCell(3).setCellValue(valuation.getCreatedAt().toString());
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        workbook.write(byteArrayOutputStream);
        workbook.close();

        return byteArrayOutputStream.toByteArray();
    }

    // Экспорт в JSON
    private byte[] exportToJson(List<ValuationDTO> valuations, Principal principal) throws JsonProcessingException {
        return objectMapper.writeValueAsBytes(valuations);
    }

    // Экспорт в PDF
    public byte[] exportToPdf(List<ValuationDTO> valuations, Principal principal) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(byteArrayOutputStream);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        for (ValuationDTO valuation : valuations) {
            document.add(new Paragraph("Valuation: " + valuation.getValuation()));
            document.add(new Paragraph("Recommendation: " + valuation.getRecommendation()));
            document.add(new Paragraph("Created at: " + valuation.getCreatedAt()));

            ScenarioDTO scenario = valuation.getScenario();
            if (scenario != null) {
                document.add(new Paragraph("Scenario: " + scenario.getName()));
                if (scenario.getCompanyName() != null) {
                    document.add(new Paragraph("Company: " + scenario.getCompanyName()));
                }
            }

            document.add(new Paragraph("------------------------------"));
        }

        document.close();
        return byteArrayOutputStream.toByteArray();
    }

}

