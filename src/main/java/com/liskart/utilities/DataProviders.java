package com.liskart.utilities;

import java.lang.reflect.Method;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.liskart.utilities.SkuJsonUtil;

import org.testng.annotations.DataProvider;

public class DataProviders {

	private static final String FILE_PATH = System.getProperty("user.dir")
			+ "/src/test/resources/testdata/TestData.xlsx";

	// ================= VALID LOGIN DATA =================
	@DataProvider(name = "validLoginData")
	public static Object[][] validLoginData(Method method) {
		return getSheetData("validLoginData", method.getName());
	}

	// ================= INVALID LOGIN DATA =================
	@DataProvider(name = "inValidLoginData")
	public static Object[][] inValidLoginData(Method method) {
		return getSheetData("inValidLoginData", method.getName());
	}

	// ================= REGISTRATION DATA =================
	@DataProvider(name = "registrationData")
	public static Object[][] registrationData(Method method) {
		return getSheetData("registrationData", method.getName());
	}

	// ================= LOGIN + EMPLOYEE DATA =================
	@DataProvider(name = "loginAndEmployeeData")
	public static Object[][] loginAndEmployeeData(Method method) {

		Object[][] loginData = getSheetData("validLoginData", method.getName());
		Object[][] empData = getSheetData("emplVerification", method.getName());

		int rows = Math.min(loginData.length, empData.length);
		int loginCols = loginData[0].length;
		int empCols = empData[0].length;

		Object[][] mergedData = new Object[rows][loginCols + empCols];

		for (int i = 0; i < rows; i++) {
			int index = 0;

			for (int j = 0; j < loginCols; j++) {
				mergedData[i][index++] = loginData[i][j];
			}

			for (int j = 0; j < empCols; j++) {
				mergedData[i][index++] = empData[i][j];
			}
		}
		return mergedData;
	}

	// ================= CORE METHOD (FILTER BY TESTCASE NAME) =================
	private static Object[][] getSheetData(String sheetName, String testCaseName) {

		List<Map<String, String>> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);

		if (sheetData == null || sheetData.isEmpty()) {
			throw new RuntimeException("No data found in Excel sheet: " + sheetName);
		}

		List<Object[]> filteredData = new ArrayList<>();


		for (Map<String, String> row : sheetData) {
			if (testCaseName.equalsIgnoreCase(row.get("TestCaseName"))) {
				// Inject a SKU from JSON (random or sequential)
				row.put("sku", SkuJsonUtil.getRandomSku()); // or use getNextSku() for sequential
				filteredData.add(new Object[] { row });
			}
		}

		if (filteredData.isEmpty()) {
			throw new RuntimeException("No data found for TestCaseName: " + testCaseName + " in sheet: " + sheetName);
		}

		return filteredData.toArray(new Object[0][]);
	}
}
