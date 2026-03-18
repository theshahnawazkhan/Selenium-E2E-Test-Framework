package com.liskart.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtility {

    public static List<Map<String, String>> getSheetData(String filePath, String sheetName) {

        List<Map<String, String>> data = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet " + sheetName + " doesn't exist in Excel");
            }

            // Read header row
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new RuntimeException("Header row is missing in sheet: " + sheetName);
            }

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {   // Skip header
                    continue;
                }

                Map<String, String> rowData = new HashMap<>();

                for (int i = 0; i < headerRow.getLastCellNum(); i++) {

                    Cell headerCell = headerRow.getCell(i);
                    Cell cell = row.getCell(i);

                    // ✅ Trim header and value to avoid blank keys/values
                    String key = headerCell.getStringCellValue().trim();
                    String value = getCellValue(cell).trim();

                    rowData.put(key, value);
                }
                data.add(rowData);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file: " + filePath, e);
        }
        return data;
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }

        switch (cell.getCellType()) {

        case STRING:
            return cell.getStringCellValue();

        case NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString();
            }

            double value = cell.getNumericCellValue();

            // ✅ Fix: remove .0 for whole numbers (password, phone, etc.)
            if (value == Math.floor(value)) {
                return String.valueOf((long) value);
            }
            return String.valueOf(value);

        case BOOLEAN:
            return String.valueOf(cell.getBooleanCellValue());

        default:
            return "";
        }
    }
}
