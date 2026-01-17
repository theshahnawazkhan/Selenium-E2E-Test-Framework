package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomeTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;

	@BeforeMethod
	public void setupPages() {
		loginPage = new LoginPage(getDriver());
		homePage = new HomePage(getDriver());
	}

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class, priority = 1)
	public void verifyOrangeHRMLogo(String username, String password) {
		ExtentManager.logStep("Navigating to login page entering username and password");
		loginPage.login(username, password);
		ExtentManager.logStep("Loggedin Successfully!");
		Assert.assertTrue(homePage.verifyOrangeHRMLogoDisplayed(), "OrangeHRM logo is not displayed");
		ExtentManager.logStep("Verify logo should display");
	}

}
