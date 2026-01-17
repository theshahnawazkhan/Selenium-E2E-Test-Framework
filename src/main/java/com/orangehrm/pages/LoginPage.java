package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	private ActionDriver actionDriver;

	// Define locator using By class

	private By userNameFieldLocator = By.xpath("//input[@name='username']");
	private By passwordFieldLocator = By.xpath("//input[@name='password']");
	private By loginBtnLocator = By.xpath("//button[@type='submit']");
	private By errorMessageLocator = By.xpath("//div[@role='alert']//p");

	public LoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public void login(String username, String password) {
		actionDriver.enterText(userNameFieldLocator, username);
		actionDriver.enterText(passwordFieldLocator, password);
		actionDriver.click(loginBtnLocator);
	}

	public boolean isErrorMessageDisplayed() {
		return actionDriver.isDisplayed(errorMessageLocator);
	}

	public String getErrorMessageText() {
		return actionDriver.getText(errorMessageLocator);

	}

	public boolean verifyErrorMessage(String expectedError) {
		return actionDriver.compareText(errorMessageLocator, expectedError);
	}

}
