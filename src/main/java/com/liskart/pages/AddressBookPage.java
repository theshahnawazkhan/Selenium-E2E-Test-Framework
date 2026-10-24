package com.liskart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.liskart.actiondriver.ActionDriver;
import com.liskart.base.BaseClass;

public class AddressBookPage {
	private ActionDriver actionDriver;

	// Define locator using By class

	private By addressBookLinkLocator = By.xpath("//span[text()='Address Book']");
	private By backBtnLocator = By.xpath("//button[@aria-label='Back']");
	private By addressBookTextLocator = By.xpath("//button[@aria-label='Back']/following::h1");
	private By addAddressBtnLocator = By.xpath("//button[text()='Add Address']");
	private By currentLocationBtnLocator = By
			.xpath("//div[@class='modal-body']/button[contains(text(),'Current Location')]");
	private By firstNameInputBoxLocator = By.xpath("//input[@name='firstname']");
	private By lastNameInputBoxLocator = By.xpath("//input[@name='lastname']");
	private By phoneNumberInputBoxLocator = By.xpath("//input[@name='phone_number']");
	private By address1InputBoxLocator = By.xpath("//input[@name='address1']");
	private By pinCodeInputBoxLocator = By.xpath("//input[@name='zip_code']");
	private By selectStateInputBoxLocator = By.xpath("//select[@name='state']");
	private By selectCityInputBoxLocator = By.xpath("//select[@name='city']");
	private By defaultShippingCheckBoxLocator = By.xpath("//input[@id='default_shipping']");
	private By submitBtnLocator = By.xpath("//button[text()='Submit']");
	private By defaultIconLocator = By.xpath("//div[text()='700160']/parent::div/preceding-sibling::div/div/span");

	public AddressBookPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public void clickOnAddressBookLink() {
		this.actionDriver.clickJS(addressBookLinkLocator);
	}

	public boolean verifyAddressBookTextDisplayed() {
		return this.actionDriver.isDisplayed(addressBookTextLocator);
	}

	public void clickOnAddAddressBtn() {
		this.actionDriver.clickJS(addAddressBtnLocator);
	}

	public void enterAddressFirstName(String addressFirstName) {
		this.actionDriver.waitForElementToBeVisible(firstNameInputBoxLocator);
		this.actionDriver.enterText(firstNameInputBoxLocator, addressFirstName);
	}

	public void enterAddressLastName(String addressLastName) {
		this.actionDriver.enterText(lastNameInputBoxLocator, addressLastName);
	}

	public void enteraddressPhone(String addressPhone) {
		this.actionDriver.enterText(phoneNumberInputBoxLocator, addressPhone);
	}

	public void enterAddress1(String address1) {
		this.actionDriver.enterText(address1InputBoxLocator, address1);
	}

	public void enterPincode(String addressPinCode) {
		this.actionDriver.enterText(pinCodeInputBoxLocator, addressPinCode);
	}

	public void selectState(String addressState) {
		this.actionDriver.selectByText(selectStateInputBoxLocator, addressState);
	}

	public void selectCity(String addressCity) {
		this.actionDriver.selectByText(selectCityInputBoxLocator, addressCity);
	}

	public void clickOnDefaultShippingCheckBox() {
		this.actionDriver.clickJS(defaultShippingCheckBoxLocator);
	}

	public void clickOnSubmitBtn() {
		this.actionDriver.clickJS(submitBtnLocator);
	}
	
	public boolean verifyDefaultIconDisplayedWithAddedAddress() {
		return this.actionDriver.isDisplayed(defaultIconLocator);
	}

	public void clickOnBacktn() {
		this.actionDriver.clickJS(backBtnLocator);
	}

	public void clickOnCurrentLocationBtn() {
		this.actionDriver.clickJS(currentLocationBtnLocator);
	}
}
