package com.orangehrm.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	@Test(dataProvider = "loginAndEmployeeData", dataProviderClass = DataProviders.class, priority = 1)
	public void verifyEmployeeNameFromDB(String username, String password, String emp_id, String emp_name) {
		
		SoftAssert softAssert = getSoftAssert();
		
		ExtentManager.logStep("Login with Admin credentials");
		loginPage.login(username, password);

		ExtentManager.logStep("Click on PIM tab");
		homePage.clickOnPIMTab();

		ExtentManager.logStep("Search for employee");
		homePage.employeeSearch(emp_name);

		ExtentManager.logStep("Get the employee name from DB");
		String employee_id = emp_id;

		// Fetch the data into a map
		Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);

		String employeeFirstName = employeeDetails.get("firstName");
		String employeeMiddleName = employeeDetails.get("middleName");
		String employeeLastName = employeeDetails.get("lastName");

		String emplFirstAndMiddleName = (employeeFirstName + " " + employeeMiddleName).trim();
		ExtentManager.logStep("Verify the employe first and middle name");
		softAssert.assertTrue(homePage.verifyEmployeeFirstAndMiddleName(emplFirstAndMiddleName),
				"First and middle name are not matching");

		ExtentManager.logStep("Verifying employee lastname");
		softAssert.assertTrue(homePage.verifyEmployeeLastName(employeeLastName), "Last name is not matching");

		ExtentManager.logStep("DB validation completed!!");
		
		softAssert.assertAll();

	}

}
