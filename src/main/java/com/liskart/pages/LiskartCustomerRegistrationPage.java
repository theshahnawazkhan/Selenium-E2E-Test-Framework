package com.liskart.pages;

import java.util.Random;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.liskart.actiondriver.ActionDriver;
import com.liskart.base.BaseClass;

public class LiskartCustomerRegistrationPage {

	private ActionDriver actionDriver;

	// Define locator using By class
	private By registrationLinkLocator = By.xpath("//a[text()='Register']");
	private By registerTextLocator = By.xpath("//h1[text()='Register']");
	private By firstNameTextBoxLocator = By.xpath("//input[@name='firstName']");
	private By lastNameTextBoxLocator = By.xpath("//input[@name='lastName']");
	private By customerProfileLocator = By.xpath("//select[@id='formUserType']");
	private By termAndConditionCheckboxLocatorn = By.xpath("//input[@id='formTermsConditions']");
	private By continueBtnLocator = By.xpath("//button[text()='Continue']");
	private By otpVerifyBtLocator = By.xpath("//button[text()='Verify']");

	public LiskartCustomerRegistrationPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public void clickOnRegistrationLik() {
		this.actionDriver.click(registrationLinkLocator);
	}

	public boolean verifyRegisterHeaderTextDisplayed() {
		return this.actionDriver.isDisplayed(registerTextLocator);
	}

	public void enterFirstName(String firstName) {
		this.actionDriver.enterText(firstNameTextBoxLocator, firstName);
	}

	public void enterLastName(String lastName) {
		this.actionDriver.enterText(lastNameTextBoxLocator, lastName);
	}

	// Generates a random 10-digit phone number as a String
	public static String generateRandomIndianPhoneNumber() {
		Random random = new Random();
		int[] validFirstDigits = { 6, 7, 8, 9 };
		int firstDigit = validFirstDigits[random.nextInt(validFirstDigits.length)];
		StringBuilder phoneNumber = new StringBuilder();
		phoneNumber.append(firstDigit);
		for (int i = 0; i < 9; i++) {
			phoneNumber.append(random.nextInt(10));
		}
		return phoneNumber.toString();
	}

	public void selectCustomerProfile(String customerProfileOption) {
		this.actionDriver.selectByText(customerProfileLocator, customerProfileOption);
	}

	public void clickOnTermAndConditionCheckBox() {
		this.actionDriver.clickJS(termAndConditionCheckboxLocatorn);
	}

	public boolean verifyContinueBtnEnabled() {
		return this.actionDriver.isElementEnabled(continueBtnLocator);
	}

	public void clickOnContinueBtn() {
		this.actionDriver.clickJS(continueBtnLocator);
	}

	public void clickOnOtpVerifyBtn() {
		this.actionDriver.click(otpVerifyBtLocator);
	}

}
