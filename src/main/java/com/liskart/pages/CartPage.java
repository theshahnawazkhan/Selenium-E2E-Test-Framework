package com.liskart.pages;

import java.nio.channels.AcceptPendingException;
import java.util.List;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

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

	// Checks if any product is present in the cart
	public boolean isProductInCart() {
		// If 'Remove item from cart' button is displayed, assume product is present
		this.actionDriver.waitForElementToBeVisible(deleteBtnLocator);
		return this.actionDriver.isDisplayed(deleteBtnLocator);
	}

	public void removeProductFromCart() {
    WebDriver driver = BaseClass.getDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

    while (true) {
        List<WebElement> deleteButtons = driver.findElements(deleteBtnLocator);

        if (deleteButtons.isEmpty()) {
            System.out.println("[INFO] All products removed.");
            break;
        }

        try {
            WebElement deleteBtn = deleteButtons.get(0); // Always pick first item
            wait.until(ExpectedConditions.elementToBeClickable(deleteBtn));
            deleteBtn.click();

            // Wait until this specific item is removed
            wait.until(ExpectedConditions.presenceOfElementLocated(yourCartIsEmptyLocator));

        } catch (Exception e) {
            System.out.println("[WARN] Retry removing item...");
        
		}
	}
    }

	public boolean verifyTotalAmountWithMiniCartAmount(By miniCartLocator) {
		String minicartAmount = this.actionDriver.getText(miniCartLocator);
		String totaAmount = this.actionDriver.getText(totalAmountLocator);
		return minicartAmount.equals(totaAmount);
	}

	public void clickOnContinueShippingBtn() {
		this.actionDriver.clickUsingActions(continueShoppingBtnLocator);
	}

	public void refreshPage() {
		BaseClass.getDriver().navigate().refresh();
	}

}
