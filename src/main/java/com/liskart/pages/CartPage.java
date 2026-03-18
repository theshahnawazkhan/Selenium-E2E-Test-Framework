package com.liskart.pages;

import java.nio.channels.AcceptPendingException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.liskart.actiondriver.ActionDriver;
import com.liskart.base.BaseClass;

public class CartPage {

	private ActionDriver actionDriver;
	
	// Define locator using By class
	
	private By minusBtnLocator = By.xpath("//button[@aria-label = 'Decrement']");
	private By plusBtnLocator = By.xpath("//button[@aria-label = 'Increment']");
	private By deleteBtnLocator = By.xpath("//button[@aria-label='Remove item from cart']");
	private By productQuantityLocator = By.xpath("//input[@aria-label='Quantity']");
	private By yourCartIsEmptyLocator = By.xpath("//h2[text()='Your cart is empty']");
	private By totalAmountLocator = By.xpath("//span[text()='Total Amount']/following-sibling::span");
	private By continueShoppingBtnLocator = By.xpath("//button[contains(text(),'Continue Shopping')]");

	public CartPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}
	
	public void increaseProductQuantity() {
		this.actionDriver.validateNumberChange(productQuantityLocator, plusBtnLocator, 1, "Product Quantity");

	}
	
	public void decreaseProductQuantity() {
		this.actionDriver.validateDecrement(productQuantityLocator, minusBtnLocator, 1, "Product Quantity");
	}
	
	public boolean removeProductFromCart() {
		this.actionDriver.clickUsingActions(deleteBtnLocator);
		return this.actionDriver.isDisplayed(yourCartIsEmptyLocator);
	}
	
	public void verifyTotalAmountWithMiniCartAmount(By miniCartLocator) {
		String minicartAmount = this.actionDriver.getText(miniCartLocator);
		String totaAmount = this.actionDriver.getText(totalAmountLocator);
		this.actionDriver.compareTextEqual(minicartAmount, totaAmount);
	}
	
	public void clickOnContinueShippingBtn() {
		this.actionDriver.clickUsingActions(continueShoppingBtnLocator);
	}

}
