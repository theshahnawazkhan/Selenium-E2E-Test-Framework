package com.liskart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.liskart.actiondriver.ActionDriver;
import com.liskart.base.BaseClass;
import com.liskart.utilities.ExtentManager;

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

	// Locators for order details (update these as per actual DOM if needed)
	private By itemPriceLocator = By.xpath("//td[contains(@class,'col-price')]/div/span");
	private By itemQuantityLocator = By.xpath("//td[contains(@class,'col-ordered-qty')]/table/tbody/tr/td");
	private By itemDiscountLocator = By.xpath("//td[contains(@class,'col-discont')]/span");
	private By itemTaxLocator = By.xpath("//td[contains(@class,'col-tax')]/span");
	private By itemRowTotalLocator = By.xpath("//td[contains(@class,'col-total')]/span");
	private By orderedTotalTaxLocator = By.xpath("//td[div[contains(text(),'Tax')]]/following-sibling::td/span");
	private By itemsOrderedSubtotalLocator = By.xpath("//td[contains(@class,'col-subtotal')]/div/span");
	private By itemTaxPercentLocator = By.xpath("//td[contains(@class,'col-tax-percent')]");

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

	public boolean verifySellerLinkDisplayed() {
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

	/**
	 * Verifies order calculations: price * quantity = subtotal, tax = 18% of
	 * subtotal,
	 * row total = subtotal - discount, row total = grand total, tax matches items
	 * ordered tax, etc.
	 * Returns true if all checks pass, false otherwise. Logs details for debugging.
	 */
	public boolean verifyOrderCalculations() {
		try {
			// Get price and quantity
			String priceText = this.actionDriver.getText(itemPriceLocator);
			String qtyText = this.actionDriver.getText(itemQuantityLocator);
			String discountText = this.actionDriver.getText(itemDiscountLocator);
			String taxText = this.actionDriver.getText(itemTaxLocator);
			String rowTotalText = this.actionDriver.getText(itemRowTotalLocator);
			String grandTotalText = this.actionDriver.getText(grandTotalPriceLocator);
			String orderedTotalTaxText = this.actionDriver.getText(orderedTotalTaxLocator);
			String itemsOrderedSubtotalText = this.actionDriver.getText(itemsOrderedSubtotalLocator);
			String taxPercentText = this.actionDriver.getText(itemTaxPercentLocator); // e.g., "18%" or "9%"

			double price = cleanAmount(priceText);
			int quantity = (int) Double.parseDouble(qtyText.replaceAll("[^0-9]", ""));
			double discount = cleanAmount(discountText);
			double tax = cleanAmount(taxText);
			double rowTotal = cleanAmount(rowTotalText);
			double grandTotal = cleanAmount(grandTotalText);
			double orderedTotalTax = cleanAmount(orderedTotalTaxText);
			double itemsOrderedSubtotal = cleanAmount(itemsOrderedSubtotalText);

			// Parse tax percent from string (e.g., "18%" or "9%")
			double taxPercent = 0.0;
			if (taxPercentText != null && !taxPercentText.isEmpty()) {
				taxPercent = Double.parseDouble(taxPercentText.replaceAll("[^0-9.]", ""));
			}

			double subtotal = price * quantity;
			double subtotalAfterDiscount = subtotal - discount;
			// Calculate taxable amount as per your formula
			double taxableAmount = subtotalAfterDiscount / (1 + taxPercent / 100.0);
			// Calculate expected tax on taxable amount
			double expectedTax = Math.round((taxableAmount * taxPercent / 100.0) * 100.0) / 100.0;

			// Logging
			String summaryMsg = "[OrderCalc] Price: " + price + ", Qty: " + quantity + ", Subtotal: " + subtotal
					+ ", Discount: " + discount + ", SubtotalAfterDiscount: " + subtotalAfterDiscount
					+ ", TaxableAmount: " + taxableAmount + ", TaxPercent: " + taxPercent
					+ ", ExpectedTax: " + expectedTax + ", Tax: " + tax;
			ActionDriver.logger.info(summaryMsg);
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Order Calculation Summary", summaryMsg);

			boolean subtotalMatch = Math.abs(subtotal - itemsOrderedSubtotal) < 0.1;
			boolean taxMatch = Math.abs(expectedTax - tax) < 0.1 && Math.abs(expectedTax - orderedTotalTax) < 0.1;
			boolean rowTotalMatch = Math.abs(subtotalAfterDiscount - rowTotal) < 0.1
					&& Math.abs(rowTotal - grandTotal) < 0.1;

			if (!subtotalMatch) {
				String msg = "[OrderCalc] Subtotal mismatch: expected " + subtotal + ", found "
						+ itemsOrderedSubtotal;
				ActionDriver.logger.warn(msg);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Subtotal Mismatch", msg);
			}
			if (!taxMatch) {
				String msg = "[OrderCalc] Tax mismatch: expected " + expectedTax + ", found item tax " + tax
						+ ", ordered total tax " + orderedTotalTax;
				ActionDriver.logger.warn(msg);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Tax Mismatch", msg);
			}
			if (!rowTotalMatch) {
				String msg = "[OrderCalc] Row total mismatch: expected " + subtotalAfterDiscount + ", found row total "
						+ rowTotal + ", grand total " + grandTotal;
				ActionDriver.logger.warn(msg);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Row Total Mismatch", msg);
			}

			return subtotalMatch && taxMatch && rowTotalMatch;
		} catch (Exception e) {
			String errMsg = "[OrderCalc] Exception: " + e.getMessage();
			ActionDriver.logger.error(errMsg, e);
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Order Calculation Exception", errMsg);
			return false;
		}
	}

	// Utility to clean and parse currency string (reuse from ShippingPage)
	private double cleanAmount(String text) {
		if (text == null)
			return 0.0;
		text = text.replaceAll("[₹,\\-−\\s]", "").trim();
		text = text.replaceAll("[()]", "");
		if (text.isEmpty())
			return 0.0;
		try {
			return Math.abs(Double.parseDouble(text));
		} catch (Exception e) {
			return 0.0;
		}
	}

}
