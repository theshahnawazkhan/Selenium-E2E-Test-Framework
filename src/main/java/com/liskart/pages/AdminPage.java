package com.liskart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.liskart.actiondriver.ActionDriver;
import com.liskart.base.BaseClass;

public class AdminPage {

	private ActionDriver actionDriver;

	// Define locator using By class

	private By usernameInputFieldLocator = By.xpath("//input[@id='username']");
	private By passwordInputFieldLocator = By.xpath("//input[@id='login']");
	private By signInBtnLocator = By.xpath("//span[contains(text(),'Sign in')]");
	private By closeModelPopupLocator = By.xpath("//aside[1]/div[2]/header/button[@data-role='closeBtn']");
	private By dashBoardTitleLocator = By.xpath("//h1[contains(text(),'Dashboard')]");
	private By salesTabLocator = By.xpath("//li[@role='menu-item']/a/span[contains(text(), 'Sales')]");
	private By ordersLinkLocator = By.xpath("//ul/li[@data-ui-id='menu-magento-sales-sales-order']/a");
	private By orderSearchInputBoxLocator = By
			.xpath("(//input[@id='fulltext' and not(contains(@style,'display: none'))])[1]");
	private By oderSearchBtnLocator = By
			.xpath("(//input[@id='fulltext' and not(contains(@style,'display: none'))])[1]/following-sibling::button");
	private By adminOrderIdLoctor = By.xpath("//*[@id=\"container\"]/div/div[4]/table/tbody/tr/td[2]/div");
	private By grandTotalPurchaseLocator = By.xpath("//*[@id=\"container\"]/div/div[4]/table/tbody/tr/td[8]/div");
	private By pendingStatusLocator = By.xpath("//div[contains(text(),'Pending')]");
	private By disapprovalApprovalStatusLocator = By.xpath("//div[contains(text(),'Disapproved')]");
	private By actionViewLocator = By.xpath("//a[@class='action-menu-item']");
	private By orderAndAccountInformationText = By.xpath("//span[contains(text(),'Order & Account Information')]");
	private By grandTotalPriceLocator = By.xpath(
			"//table[@class='data-table admin__table-secondary order-subtotal-table']//tr[1]/td[2]/strong/span[@class='price']");
	private By totalDuePriceLocator = By.xpath(
			"//table[@class='data-table admin__table-secondary order-subtotal-table']//tr[4]/td[2]/strong/span[@class='price']");
	private By sellerLinkLocator = By.xpath("//strong[contains(text(),'By Seller:')]/following-sibling::a");
	private By sellerPersonalInformationHeaderTextLocator = By.xpath("//span[contains(text(),'Personal Information')]");
	private By sellerAccountInformationLinkLocator = By.xpath("//a[@id='tab_customer']");
	private By sellerPhoneNumberInputBoxLocator = By.xpath("//div[@data-index='phone_number']//input");

	public AdminPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public void enterAdminUserName(String adminUserName) {
		this.actionDriver.enterText(usernameInputFieldLocator, adminUserName);
	}

	public void enterAdminPassword(String adminPassword) {
		this.actionDriver.enterText(passwordInputFieldLocator, adminPassword);
	}

	public void clickOnAdminSignInBtn() {
		this.actionDriver.click(signInBtnLocator);
	}

	public void closeModelPop() {
		this.actionDriver.clickJS(closeModelPopupLocator);
	}

	public boolean verifyAdminDashoadTitleDisplayed() {
		return this.actionDriver.isDisplayed(dashBoardTitleLocator);
	}

	public void clickOnSalesTab() {
		this.actionDriver.clickJS(salesTabLocator);
	}

	public void clickOnOrdersLink() {
		this.actionDriver.clickJS(ordersLinkLocator);
	}

	public void clearOrderInputTextBox() {
		this.actionDriver.clearText(orderSearchInputBoxLocator);
	}

	public void enterOrderID(String orderId) {
		this.actionDriver.enterText(orderSearchInputBoxLocator, orderId);
	}

	public void clickOnOrderSearchBtn() {
		this.actionDriver.clickJS(oderSearchBtnLocator);
	}

	public boolean verifyOrderStatusDisplayed() {
		return this.actionDriver.isDisplayed(pendingStatusLocator);
	}

	public boolean verifyApprovalStatusDisplayed() {
		return this.actionDriver.isDisplayed(disapprovalApprovalStatusLocator);
	}

	public void clickOnView() {
		this.actionDriver.clickUsingActions(actionViewLocator);
	}

	public void refreshUntilElementDisplayed() {
		this.actionDriver.refreshUntilElementDisplayed(orderAndAccountInformationText, 10);
	}

	public void compareGrandTotalAndTotalDuePrice() {
		String grandTotalPrice = this.actionDriver.getText(grandTotalPriceLocator);
		String totalDueprice = this.actionDriver.getText(totalDuePriceLocator);
		this.actionDriver.compareTextEqual(grandTotalPrice, totalDueprice);
	}

	public boolean verifyElementDisplayed() {
		this.actionDriver.scrollToElement(sellerLinkLocator);
		return this.actionDriver.isDisplayed(sellerLinkLocator);
	}

	public void clickOnSellerLink() {
		this.actionDriver.clickUsingActions(sellerLinkLocator);
	}

	public void refreshUntilSellerInformationDisplayed() {
		this.actionDriver.refreshUntilElementDisplayed(sellerPersonalInformationHeaderTextLocator, 10);
	}

	public void clickOnSellerAccountInformationLink() {
		this.actionDriver.clickUsingActions(sellerAccountInformationLinkLocator);
	}

	public void scrollToSellerPhoneInputBox() {
		this.actionDriver.scrollToElement(sellerPhoneNumberInputBoxLocator);
	}

	public String getSellerPhoneNumber() {
		return this.actionDriver.getInputValueUsingDomProperty(sellerPhoneNumberInputBoxLocator);
	}

}
