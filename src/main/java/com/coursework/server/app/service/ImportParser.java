package com.coursework.server.app.service;

import com.coursework.server.app.dto.FinancialRow;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ImportParser {

    public List<FinancialRow> parse(MultipartFile file) throws IOException, CsvValidationException {
        String fileType = getFileType(file);
        switch (fileType) {
            case "csv":
                return parseCsv(file);
            case "xlsx":
                return parseXlsx(file);
            case "json":
                return parseJson(file);
            default:
                throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
    }

    private String getFileType(MultipartFile file) {
        String fileName = file.getOriginalFilename().toLowerCase();
        if (fileName.endsWith(".csv")) {
            return "csv";
        } else if (fileName.endsWith(".xlsx")) {
            return "xlsx";
        } else if (fileName.endsWith(".json")) {
            return "json";
        } else {
            throw new IllegalArgumentException("Unsupported file extension: " + file.getOriginalFilename());
        }
    }

    private List<FinancialRow> parseCsv(MultipartFile file) throws IOException, CsvValidationException {
        List<FinancialRow> rows = new ArrayList<>();
        try (InputStreamReader isr = new InputStreamReader(file.getInputStream());
             CSVReader csvReader = new CSVReader(isr)) {

            String[] nextLine;
            while ((nextLine = csvReader.readNext()) != null) {
                // Парсим строку: year, equity, netIncome, discountRate
                FinancialRow row = new FinancialRow();
                row.setYear(Integer.parseInt(nextLine[0]));
                row.setEquity(new BigDecimal(nextLine[1]));
                row.setNetIncome(new BigDecimal(nextLine[2]));
                if (nextLine.length > 3) {
                    row.setDiscountRate(new BigDecimal(nextLine[3]));
                }
                rows.add(row);
            }
        }
        return rows;
    }

    private List<FinancialRow> parseXlsx(MultipartFile file) throws IOException {
        List<FinancialRow> rows = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // Пропускаем заголовок
                }
                FinancialRow financialRow = new FinancialRow();
                financialRow.setYear((int) row.getCell(0).getNumericCellValue());
                financialRow.setEquity(BigDecimal.valueOf(row.getCell(1).getNumericCellValue()));
                financialRow.setNetIncome(BigDecimal.valueOf(row.getCell(2).getNumericCellValue()));

                if (row.getCell(3) != null) {
                    financialRow.setDiscountRate(BigDecimal.valueOf(row.getCell(3).getNumericCellValue()));
                }

                rows.add(financialRow);
            }
        }
        return rows;
    }

    private List<FinancialRow> parseJson(MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file.getInputStream(), objectMapper.getTypeFactory().constructCollectionType(List.class, FinancialRow.class));
    }
}
