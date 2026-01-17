package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {

	private ActionDriver actionDriver;

	private By adminTabLocator = By.xpath("//span[text()='Admin']");
	private By userIdBtnLocator = By.xpath("//p[@class='oxd-userdropdown-name']");
	private By logoutBtnLocator = By.xpath("//a[@class='oxd-userdropdown-link' and text()='Logout']");
	private By orangeHRMLogoLocator = By.xpath("//div[@class='oxd-brand-banner']/img");
	private By pimTabLocator = By.xpath("//span[text()='PIM']");
	private By employeeNameTextBoxLocator = By
			.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div//input");
	private By searchBtnLocator = By.xpath("//button[@type='submit']");
	private By empFirstAndMiddleNameLocator = By.xpath("//div[@class='oxd-table-card']//div[3]/div");
	private By empLastNameLocator = By.xpath("//div[@class='oxd-table-card']//div[4]/div");

	// Initialize the ActionDriver object by passing WebDriver
	public HomePage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	// Method to verify if admin tab is visible
	public boolean isAdminTabVisible() {
		return actionDriver.isDisplayed(adminTabLocator);
	}

	public boolean verifyOrangeHRMLogoDisplayed() {
		return actionDriver.isDisplayed(orangeHRMLogoLocator);
	}

	public void logOut() {
		actionDriver.click(userIdBtnLocator);
		actionDriver.click(logoutBtnLocator);
	}

	// Method to navigate to PIM tab
	public void clickOnPIMTab() {
		actionDriver.click(pimTabLocator);
	}

	// Employee Search Method
	public void employeeSearch(String value) {
		actionDriver.enterText(employeeNameTextBoxLocator, value);
		actionDriver.click(searchBtnLocator);
		actionDriver.scrollToElement(empFirstAndMiddleNameLocator);
	}

	// Verify employee first and middle name
	public boolean verifyEmployeeFirstAndMiddleName(String emplFirstAndMiddleNameFromDB) {
		return actionDriver.compareText(empFirstAndMiddleNameLocator, emplFirstAndMiddleNameFromDB);
	}
	// Verify employee last name
	public boolean verifyEmployeeLastName(String LastNameFromDB) {
		return actionDriver.compareText(empLastNameLocator, LastNameFromDB);
	}

}
