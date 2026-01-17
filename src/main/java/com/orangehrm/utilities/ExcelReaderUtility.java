package com.orangehrm.utilities;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtility {

	public static List<String[]> getSheetData(String filePath, String sheetName) {
		List<String[]> data = new ArrayList<>();

		try (FileInputStream fis = new FileInputStream(filePath);
			 Workbook workbook = new XSSFWorkbook(fis)) {

			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new IllegalArgumentException("Sheet " + sheetName + " doesn't exist in Excel");
			}

			for (Row row : sheet) {
				if (row.getRowNum() == 0) {   // Skip header
					continue;
				}
				List<String> rowData = new ArrayList<>();
				for (Cell cell : row) {
					rowData.add(getCellValue(cell));
				}
				data.add(rowData.toArray(new String[0]));
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
			return String.valueOf(cell.getNumericCellValue());

		case BOOLEAN:
			return String.valueOf(cell.getBooleanCellValue());

		default:
			return "";
		}
	}
}
