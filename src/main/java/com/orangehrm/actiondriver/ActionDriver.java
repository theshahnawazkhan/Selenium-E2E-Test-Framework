package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	private JavascriptExecutor js;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
		logger.info("WebDriver instance is created.");
	}

	// wait for element to be clickable
	private void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element is not clickable:" + e.getMessage());
		}
	}

	// wait for element to be visible
	private void waitForElementToBeVisisble(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
		} catch (Exception e) {
			logger.error("Element is not visible:" + e.getMessage());
		}
	}

	// Method to click on element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeClickable(by);
			applyBorder(by, "green");
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked an element" + elementDescription);
			logger.info("Clicked an element -->" + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element:",
					elementDescription + "_unable to click");
			logger.error("Unable to click on element:" + e.getMessage());
		}
	}

	// Method to enter text into an input field
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisisble(by);
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);
			element.clear();
			element.sendKeys(value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable enter the value into input field:" + e.getMessage());
		}
	}

	// Method to get the text
	public String getText(By by) {
		try {
			waitForElementToBeVisisble(by);
			applyBorder(by, "green");
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get the text:" + e.getMessage());
			return "";
		}
	}

	// Method to compare two text
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisisble(by);
			String actualText = driver.findElement(by).getText();

			if (expectedText.equals(actualText)) {
				applyBorder(by, "green");
				logger.info("Text are matching: " + actualText + " equals " + expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Text verified Successfully! " + actualText + " equals " + expectedText);
				return true;
			} else {
				applyBorder(by, "red");
				logger.error("Text are not matching: " + actualText + " not equals " + expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), "Text comparison failed ",
						"Text comparison failed! " + actualText + " not equals " + expectedText);
				return false;
			}
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to compare the text: " + e.getMessage());
			return false;
		}
	}

	// Method to check if element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitForElementToBeVisisble(by);
			applyBorder(by, "green");
			logger.info("Element is displayed: " + getElementDescription(by));
			ExtentManager.logStep("Element is displayed: " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Element is not displayed:" + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element is not displayed: ", getElementDescription(by));
			return false;
		}
	}

	// scroll to an element
	public void scrollToElement(By by) {
		try {
			waitForElementToBeVisisble(by);
			applyBorder(by, "green");
			js = ((JavascriptExecutor) driver);
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to scroll into element using JavscriptExecutor:" + e.getMessage());
		}
	}

	// wait for the page to load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));

			logger.info("Page loaded successfully");
		} catch (Exception e) {
			logger.error("Page did not load within " + timeOutInSec + " seconds. Exeption:" + e.getMessage());
		}
	}

	// Method to get the description of an element using By locator
	public String getElementDescription(By locator) {
		if (driver == null) {
			return "driver is null";
		}
		if (locator == null)
			return "Locator is null";

		WebElement element = driver.findElement(locator);

		try {
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String className = element.getDomAttribute("class");
			String placeHolder = element.getDomAttribute("placeholder");

			if (isNotEmpty(name)) {
				return "Element with  name:" + name;
			} else if (isNotEmpty(id)) {
				return "Element with  id:" + id;
			} else if (isNotEmpty(text)) {
				return "Element with text:" + text;
			} else if (isNotEmpty(className)) {
				return "Element with className:" + className;
			} else if (isNotEmpty(placeHolder)) {
				return "Element with placeholder:" + placeHolder;
			}
		} catch (Exception e) {
			logger.error("Unable to describe the element" + e.getMessage());
		}

		return "Unable to describe the element";

	}

	private boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	private String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}
		return value.substring(0, maxLength) + "...";
	}

	// Utility method to border an element
	public void applyBorder(By by, String color) {
		try {
			WebElement element = driver.findElement(by);

			String script = "arguments[0].style.border='3px solid " + color + "'";
			js = (JavascriptExecutor) driver;
			js.executeScript(script, element);

			logger.info("Applied the border with color " + color + " to element " + getElementDescription(by));
		} catch (Exception e) {
			logger.warn("Failed to apply the border to an element: " + getElementDescription(by), e);
		}
	}

}
