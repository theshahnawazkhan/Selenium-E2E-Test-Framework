package com.orangehrm.test;

import static org.testng.Assert.assertTrue;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class, priority =1)
	public void verifyLoginTest(String username, String password) {
		loginPage.login(username, password);
		ExtentManager.logStep("Navigating to login page entering username and password");
		Assert.assertTrue(homePage.isAdminTabVisible(), "OrangeHRM logo should be visible after login successfully.");
		ExtentManager.logStep("Verify admin tab should visible");
		ExtentManager.logStep("Validation Succesfully!");
		homePage.logOut();
		ExtentManager.logStep("Logout Succesfully!");
		staticWait(2);
	}

	@Test(dataProvider = "inValidLoginData", dataProviderClass = DataProviders.class, priority =2)
	public void verifyInvalidLoginTest(String username, String password) {
		ExtentManager.logStep("Navigating to login page entering username and password");
		loginPage.login(username, password);
		String expectedErrorMsg = "Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expectedErrorMsg), "Valid credetial");
		ExtentManager.logStep("Validation Succesfully!");
	}

}
