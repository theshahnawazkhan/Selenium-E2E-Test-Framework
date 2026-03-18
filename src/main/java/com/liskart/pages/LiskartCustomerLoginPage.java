package com.liskart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.liskart.actiondriver.ActionDriver;
import com.liskart.base.BaseClass;

public class LiskartCustomerLoginPage {

	private ActionDriver actionDriver;

	// Define locator using By class

	private By lisKartTitleLocator = By.xpath("//img[@title='Liskart']");
	private By loginRegisterLinkLocator = By.xpath("//span[text()='Login/Register']");
	private By phoneNumberTextLocator = By.xpath("//input[@id='formPhoneNumber']");
	private By sendOtpBtnLocator = By.xpath("//button[text()='Send OTP']");
	private By OtpTextLocator = By.xpath("//input[@id='formOtp']");
	private By loginBtnLocator = By.xpath("//button[@type='submit' and text()='Login']");
	private By userLinkLocator = By.xpath("//div[@id='localSwitcher']/preceding-sibling::div//span");
	private By signOutLocator = By.xpath("//span[contains(text(),'Sign Out')]");

	public LiskartCustomerLoginPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public boolean verifyLogoDisplayed() {
		return actionDriver.isDisplayed(lisKartTitleLocator);
	}

	public void clickOnLoginRegisterLink() {
		this.actionDriver.clickUsingActions(loginRegisterLinkLocator);
	}

	public void enterPhoneNumber(String phone) {
		this.actionDriver.enterText(phoneNumberTextLocator, phone);
	}

	public void clickOnSendOtpBtn() {
		this.actionDriver.waitForElementToBeClickable(sendOtpBtnLocator);
		this.actionDriver.clickJS(sendOtpBtnLocator);
	}

	public void clickOnLoginBtn() {
		this.actionDriver.click(loginBtnLocator);
	}

	public void enterOtp(String otp) {
		this.actionDriver.enterText(OtpTextLocator, otp);
	}

	public void login(String phone, String otp) throws InterruptedException {

		verifyLogoDisplayed();
		clickOnLoginRegisterLink();

		enterPhoneNumber(phone);
		clickOnSendOtpBtn();

		Thread.sleep(5000);
		// OTP will be captured in TEST using GraphQL
		enterOtp(otp);
		clickOnLoginBtn();
	}

	public void clickOnUser() {
		this.actionDriver.waitForElementToBeClickable(userLinkLocator);
		this.actionDriver.click(userLinkLocator);
	}

	public void clickonSignOutLink() {
		this.actionDriver.click(signOutLocator);
	}

	public boolean verifyLoginRegisterLinkDisplayed() {
		return this.actionDriver.isDisplayed(loginRegisterLinkLocator);
	}

	public boolean verifyUserLinkDisplayed() {
		return this.actionDriver.isDisplayed(userLinkLocator);
	}

}
