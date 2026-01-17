package com.orangehrm.utilities;

import java.util.List;
import org.testng.annotations.DataProvider;

public class DataProviders {

	private static final String FILE_PATH = System.getProperty("user.dir")
			+ "/src/test/resources/testdata/TestData.xlsx";

	@DataProvider(name = "validLoginData")
	public static Object[][] validLoginData() {
		return getSheetData("validLoginData");
	}

	@DataProvider(name = "inValidLoginData")
	public static Object[][] inValidLoginData() {
		return getSheetData("inValidLoginData");
	}
	
	@DataProvider(name = "loginAndEmployeeData")
	public static Object[][] loginAndEmployeeData() {

	    Object[][] loginData = getSheetData("validLoginData");
	    Object[][] empData   = getSheetData("emplVerification");

	    int rows = Math.min(loginData.length, empData.length);
	    int loginCols = loginData[0].length;
	    int empCols   = empData[0].length;

	    Object[][] mergedData = new Object[rows][loginCols + empCols];

	    for (int i = 0; i < rows; i++) {
	        int index = 0;

	        // validLoginData columns
	        for (int j = 0; j < loginCols; j++) {
	            mergedData[i][index++] = loginData[i][j];
	        }

	        // emplVerification columns
	        for (int j = 0; j < empCols; j++) {
	            mergedData[i][index++] = empData[i][j];
	        }
	    }
	    return mergedData;
	}


	private static Object[][] getSheetData(String sheetName) {

		List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);

		if (sheetData == null || sheetData.isEmpty()) {
			throw new RuntimeException("No data found in Excel sheet: " + sheetName);
		}

		Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];

		for (int i = 0; i < sheetData.size(); i++) {
			data[i] = sheetData.get(i);
		}
		return data;
	}
}
