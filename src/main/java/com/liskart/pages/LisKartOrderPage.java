package com.liskart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.liskart.actiondriver.ActionDriver;
import com.liskart.base.BaseClass;
import com.liskart.utilities.ExtentManager;

public class LisKartOrderPage {

	private ActionDriver actionDriver;

	// Define locator using By class

	private By searchTextFieldLocator = By.xpath("//input[@aria-label = 'Search']");
	private By searchBtnLocator = By.xpath("//button[@type= 'submit' and @aria-label = 'Search']");
	private By productLocator = By.xpath("//ul[@class='list-unstyled mb-0']/li/a/span/span[1]");
	private By partNumberLocator = By.xpath("//p[text()='Part Number']/following-sibling::p");
	private By plusBtnLocator = By.xpath("//button[@aria-label = 'Increment']");
	private By minusBtnLocator = By.xpath("//button[@aria-label = 'Decrement']");
	private By addToCartBtnLocator = By.xpath("//button[text() = 'Add to cart']");
	private By buyNowBtnLocator = By.xpath("//button[text() = 'Buy Now']");
	private By cartLocator = By.xpath("//div[contains(@class,'MiniCart_cartIcon')]/following-sibling::span");
	private By proceedToCheckoutLocator = By.xpath("//button[text()='Proceed to Checkout']");
	private By cashOnDeliveryInputLocator = By.xpath("//input[@id='cashondelivery']");
	private By creditCardInputLocator = By.xpath("//input[@id='jiopay']");
	private By selectCouponLinkLocator = By.xpath("//button[text()='Select']");
	private By selectCouponLocator = By.xpath("//span[text()='WELCOME100']/preceding-sibling::div/input");
	private By applyCouponBtnLocator = By.xpath("//button[text()='Apply Coupon']");
	private By removeCouponLocator = By.xpath("//button[@aria-label='Remove Coupon']");
	private By placeOrderBtnLocator = By.xpath("//button[text()='Place Order']");
	private By thankYouMsgLocator = By.xpath("//p[text()='Thank you for purchasing our product']");
	private By viewOrdersBtnLocator = By.xpath("//button[text()='View Orders']");
	private By oderIdTextLocator = By.xpath("//form/following-sibling::div[1]//h2[contains(text(), 'Order ID')]");
	private By orderViewBtnLocator = By.xpath("//button[text()='View']");

	public LisKartOrderPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public void searchSKU(String sku) {
		this.actionDriver.enterText(searchTextFieldLocator, sku);
	}

	public void clickOnSearchBtn() {
		this.actionDriver.click(searchBtnLocator);
	}

	public void clickOnProduct() {
		try {
			if (BaseClass.getDriver().findElements(productLocator).size() > 0) {
				this.actionDriver.waitForElementToBeVisible(productLocator);
				this.actionDriver.clickJS(productLocator);
				ActionDriver.logger.info("[clickOnProduct] Product element found and clicked.");
			} else {
				ActionDriver.logger.warn("[clickOnProduct] Product element not found. Skipping click.");
			}
		} catch (Exception e) {
			ActionDriver.logger.error("[clickOnProduct] Exception occurred: " + e.getMessage(), e);
		}
	}

	public boolean verifyProductSKU(String sku) {
		return this.actionDriver.compareText(partNumberLocator, sku);
	}

	public void addOrder(int clickNumber) {
		this.actionDriver.waitForElementToBeVisible(plusBtnLocator);
		this.actionDriver.clickElementMultipleTimes(plusBtnLocator, clickNumber);
	}

	public void clickOnAddToCartBtn() {
		this.actionDriver.click(addToCartBtnLocator);
	}

	public void clickOnCart() {
		this.actionDriver.clickJS(getCartLocator());
	}

	public void clickOnProceedToCheckoOut() {
		this.actionDriver.clickJS(proceedToCheckoutLocator);
		this.actionDriver.waitForElementToBeClickable(cashOnDeliveryInputLocator);
	}

	public void clickOnCashOnDeliveryInputBox() {
		this.actionDriver.clickJS(cashOnDeliveryInputLocator);
	}

	public void clickOnSelectCouponLink() {
		this.actionDriver.waitForElementToBeClickable(selectCouponLinkLocator);
		this.actionDriver.clickJS(selectCouponLinkLocator);
	}

	public void clickOnCoupon() {
		this.actionDriver.waitForElementToBeClickable(selectCouponLocator);
		this.actionDriver.clickJS(selectCouponLocator);
	}

	public void clickOnApplyCouponBtn() {
		this.actionDriver.clickJS(applyCouponBtnLocator);
	}

	/**
	 * Applies coupon if not already applied. Logs each step.
	 */
	public void applyCouponIfNotPresent() {
		if (BaseClass.getDriver().findElements(removeCouponLocator).size() > 0) {
			ActionDriver.logger.info("Coupon already applied");
			ExtentManager.logStep("Coupon already applied");
		} else {
			ExtentManager.logStep("Applying coupon: clicking Select Coupon link");
			clickOnSelectCouponLink();
			ExtentManager.logStep("Selecting coupon");
			clickOnCoupon();
			ExtentManager.logStep("Clicking Apply Coupon button");
			clickOnApplyCouponBtn();
			ExtentManager.logStep("Coupon applied");
		}
	}

	public void clickOnPlaceOrderBtn() {
		this.actionDriver.clickJS(placeOrderBtnLocator);
	}

	public boolean verifyPlacedThankYouMsg(String expectedText) {
		return this.actionDriver.compareText(thankYouMsgLocator, expectedText);

	}

	public void clickOnViewOrdersBtn() {
		this.actionDriver.clickUsingActions(viewOrdersBtnLocator);
	}

	public String getOrderId() {
		String orderId = this.actionDriver.getText(oderIdTextLocator);
		if (orderId != null && orderId.contains(":")) {
			return orderId.substring(orderId.indexOf(":") + 1).trim();
		}

		throw new RuntimeException("Order ID format not correct: " + orderId);
	}

	public void clickOnOrderViewBtn() {
		this.actionDriver.waitForElementToBeClickable(orderViewBtnLocator);
		this.actionDriver.clickJS(orderViewBtnLocator);
	}

	/**
	 * Clears the SKU search input field using JS and triggers input/change events.
	 */
	public void clearSearchField() {
		try {
			this.actionDriver.waitForElementToBeVisible(searchTextFieldLocator);
			org.openqa.selenium.WebElement element = com.liskart.base.BaseClass.getDriver().findElement(searchTextFieldLocator);
			org.openqa.selenium.JavascriptExecutor js = (org.openqa.selenium.JavascriptExecutor) com.liskart.base.BaseClass.getDriver();
			js.executeScript("arguments[0].value = ''; arguments[0].dispatchEvent(new Event('input', {bubbles:true})); arguments[0].dispatchEvent(new Event('change', {bubbles:true}));", element);
			com.liskart.actiondriver.ActionDriver.logger.info("[clearSearchField] Cleared search input using JS");
		} catch (Exception e) {
			com.liskart.actiondriver.ActionDriver.logger.error("[clearSearchField] Failed to clear search input: " + e.getMessage(), e);
		}
	}

	public By getCartLocator() {
		return cartLocator;
	}
}
