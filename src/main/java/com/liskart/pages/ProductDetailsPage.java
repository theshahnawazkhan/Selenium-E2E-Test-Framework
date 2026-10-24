package com.liskart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.liskart.actiondriver.ActionDriver;
import com.liskart.base.BaseClass;

public class ProductDetailsPage {

	private ActionDriver actionDriver;

	// Define locator using By class

	private By outOfStockLocator = By
			.xpath("//p[text()='Part Number']/parent::div/parent::div/following-sibling::span[text()='Out of Stock']");
	private By notifyMeBtnLocator = By.xpath(
			"//p[text()='Part Number']/parent::div/parent::div/following-sibling::span[text()='Out of Stock']/following-sibling::div/button[text()='Notify Me']");

	public ProductDetailsPage(WebDriver driver) {
		this.actionDriver = BaseClass.getActionDriver();
	}

	public boolean verifyOutOfStockTextDisplayed() {
		return this.actionDriver.isDisplayed(outOfStockLocator);
	}

	public boolean verifyNotifyMeBtnDisplayed() {
		return this.actionDriver.isDisplayed(notifyMeBtnLocator);
	}

}
