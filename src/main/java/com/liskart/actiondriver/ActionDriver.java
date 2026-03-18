package com.liskart.actiondriver;

import java.time.Duration;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.liskart.base.BaseClass;
import com.liskart.utilities.ExtentManager;

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
	public void waitForElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.error("Element is not clickable:" + e.getMessage());
		}
	}

	// wait for element to be visible
	public void waitForElementToBeVisible(By by) {
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

	// Method to click on element using Actions class
	public void clickUsingActions(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeClickable(by);
			applyBorder(by, "green");

			WebElement element = driver.findElement(by);
			Actions actions = new Actions(driver);
			actions.moveToElement(element).click().build().perform();

			ExtentManager.logStep("Clicked an element " + elementDescription);
			logger.info("Clicked an element --> " + elementDescription);

		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element:",
					elementDescription + "_unable to click");
			logger.error("Unable to click on element: " + e.getMessage());
		}
	}

	// Click element using JavaScript
	public void clickJS(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitForElementToBeClickable(by);
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);

			// JS click
			((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);

			ExtentManager.logStep("Clicked an element: " + elementDescription);
			logger.info("Clicked an element --> " + elementDescription);
		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element:",
					elementDescription + "_unable to click");
			logger.error("Unable to click on element using JS: " + e.getMessage());
		}
	}

	// Method to enter text into an input field
	public void enterText(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);
			element.sendKeys(value);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable enter the value into input field:" + e.getMessage());
		}
	}

	// Method to enter text using JS and trigger all necessary events for dropdowns
	public void enterTextJS(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			WebElement element = driver.findElement(by);

			// JS script to simulate typing like a human
			String script = "var el = arguments[0];" + "el.focus();" + "el.value = '';" + // clear existing value
					"for(var i=0; i<arguments[1].length; i++) {" + "  var char = arguments[1].charAt(i);"
					+ "  el.value += char;"
					+ "  el.dispatchEvent(new KeyboardEvent('keydown', {bubbles:true, key:char}));"
					+ "  el.dispatchEvent(new KeyboardEvent('keypress', {bubbles:true, key:char}));"
					+ "  el.dispatchEvent(new KeyboardEvent('keyup', {bubbles:true, key:char}));"
					+ "  el.dispatchEvent(new Event('input', {bubbles:true}));" + "}"
					+ "el.dispatchEvent(new Event('change', {bubbles:true}));";

			((JavascriptExecutor) driver).executeScript(script, element, value);

			logger.info("Entered value '" + value + "' into " + by + " using JS");

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Failed to enter value using JS: " + e.getMessage());
		}
	}

	public void enterTextUsingActions(By by, String value) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");

			WebElement element = driver.findElement(by);
			element.click();
			Actions actions = new Actions(driver);
			actions.sendKeys(element, value).perform(); // Type like real user

			logger.info("Entered value '" + value + "' into " + by + " using Actions class");

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Failed to enter value using Actions: " + e.getMessage());
		}
	}

	// Method to get the text
	public String getText(By by) {
		String elementDescription = getElementDescription(by);

		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			String text = driver.findElement(by).getText();
			logger.info("Fetched text from element --> " + elementDescription + " : " + text);
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Get Text",
					"Fetched text from element --> " + elementDescription + " : " + text);
			return text;

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get text from element --> " + elementDescription + " Error: " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Get Text Failed",
					"Unable to fetch text from element --> " + elementDescription);
			return "";
		}
	}

	// Method to get attribute value
	public String getAttributeValue(By by, String attributeName) {
		try {
			waitForElementToBeVisible(by);
			applyBorder(by, "green");
			return driver.findElement(by).getAttribute(attributeName).trim();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Unable to get attribute value: " + e.getMessage());
			return "";
		}
	}

	// Method to compare two text
	public boolean compareText(By by, String expectedText) {
		try {
			waitForElementToBeVisible(by);
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
			waitForElementToBeVisible(by);
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
			waitForElementToBeVisible(by);
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

	public void clickPasswordPopupOkButton() {
		try {
			// Wait until the popup is visible
			By popupLocator = By.cssSelector(".change-password-modal"); // replace with your popup class
			waitForElementToBeVisible(popupLocator);

			// JS to find the OK button inside the popup and click it
			String script = "var popup = document.querySelector('.change-password-modal');" + // popup container
					"if(popup) {" + "  var buttons = popup.getElementsByTagName('button');"
					+ "  for(var i=0; i<buttons.length; i++) {" + "    if(buttons[i].innerText.trim() === 'OK') {"
					+ "       buttons[i].click();" + "       break;" + "    }" + "  }" + "}";

			((JavascriptExecutor) driver).executeScript(script);

			logger.info("Clicked OK button on Change Password popup using JS");

		} catch (Exception e) {
			logger.error("Failed to click OK button on Change Password popup: " + e.getMessage());
		}
	}

	// Common method to press BACKSPACE key
	public void pressBackspace(By by) {
		try {
			WebElement element = driver.findElement(by); // target element
			waitForElementToBeVisible(by); // optional: wait
			Actions actions = new Actions(driver);
			actions.click(element) // focus on the element
					.sendKeys(Keys.BACK_SPACE) // press backspace
					.perform();

			logger.info("Pressed BACKSPACE key on element: " + getElementDescription(by));
			ExtentManager.logStep("Pressed BACKSPACE key on element: " + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Failed to press BACKSPACE key: " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to press BACKSPACE",
					"Failed to press BACKSPACE key on element: " + getElementDescription(by));
		}
	}

	public List<WebElement> findElements(By locator) {
		return BaseClass.getDriver().findElements(locator);
	}

	public void selectOption(By dropdown, String option, String selectBy) {
		try {
			waitForElementToBeVisible(dropdown);
			applyBorder(dropdown, "green");
			Select select = new Select(driver.findElement(dropdown));

			if (selectBy.equalsIgnoreCase("text"))
				select.selectByVisibleText(option);
			else if (selectBy.equalsIgnoreCase("value"))
				select.selectByValue(option);
			else
				select.selectByIndex(Integer.parseInt(option));

		} catch (Exception e) {
			applyBorder(dropdown, "red");
			logger.error("Dropdown selection failed: " + e.getMessage());
			throw e;
		}
	}

	// Method to click on element multiple times using normal click
	public void clickElementMultipleTimes(By by, int numberOfClicks) {
		String elementDescription = getElementDescription(by);

		try {
			for (int i = 1; i <= numberOfClicks; i++) {
				waitForElementToBeClickable(by);

				WebElement element = driver.findElement(by);
				applyBorder(by, "green");
				element.click();

				logger.info("Clicked " + i + " time(s) on element --> " + elementDescription);
			}

			ExtentManager.logStep("Clicked element " + elementDescription + " " + numberOfClicks + " times");

		} catch (Exception e) {
			applyBorder(by, "red");
			ExtentManager.logFailure(BaseClass.getDriver(), "Unable to click element:",
					elementDescription + "_unable to click");
			logger.error("Unable to click on element: " + e.getMessage());
		}
	}

	// Method to select element by text
	public void selectByText(By by, String text) {
		String elementDescription = getElementDescription(by);
		try {
			logger.info("Waiting for dropdown to be clickable: " + by);
			WebElement element = new WebDriverWait(driver, Duration.ofSeconds(50))
					.until(ExpectedConditions.elementToBeClickable(by));

			applyBorder(by, "green");
			logger.info("Selecting option: " + text);
			ExtentManager.logStep("Selecting '" + text + "' from dropdown");

			element.click();
			Select select = new Select(element);
			select.selectByVisibleText(text);

			// trigger JS change event
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].dispatchEvent(new Event('change'))", element);

			logger.info("Dropdown selection successful: " + text);

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Dropdown selection failed: " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Dropdown Selection Failed:",
					elementDescription + "_unable to select");

		}
	}

	// Method to check element is enabled
	public boolean isElementEnabled(By by) {
		String elementDescription = getElementDescription(by);
		try {
			logger.info("Waiting for element to be enabled: " + by);

			WebElement element = new WebDriverWait(driver, Duration.ofSeconds(30))
					.until(ExpectedConditions.elementToBeClickable(by));

			applyBorder(by, "green");
			ExtentManager.logStep("Verified element is enabled");

			return element.isEnabled();

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("Element not enabled: " + e.getMessage());

			ExtentManager.logFailure(BaseClass.getDriver(), "Element Not Enabled:",
					elementDescription + "_element disabled or not clickable");

			return false;
		}
	}

	// Method to check two text are not equal
	public void compareTextNotEqual(String text1, String text2) {
		try {
			if (java.util.Objects.equals(text1, text2)) {
				throw new Exception("Texts are equal");
			}

			logger.info("Verified: '" + text1 + "' not equals '" + text2 + "'");
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text (Not Equal)",
					"Verified: '" + text1 + "' not equals '" + text2 + "'");

		} catch (Exception e) {
			logger.error("Failed: '" + text1 + "' equals '" + text2 + "'");
			ExtentManager.logFailure(BaseClass.getDriver(), "Text comparison failed",
					"Failed: '" + text1 + "' equals '" + text2 + "'");
		}
	}

	// Method to check two texts are equal
	public void compareTextEqual(String text1, String text2) {
		try {
			if (!java.util.Objects.equals(text1, text2)) {
				throw new Exception("Texts are not equal");
			}

			logger.info("Verified: '" + text1 + "' equals '" + text2 + "'");
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text (Equal)",
					"Verified: '" + text1 + "' equals '" + text2 + "'");

		} catch (Exception e) {
			logger.error("Failed: '" + text1 + "' not equals '" + text2 + "'");
			ExtentManager.logFailure(BaseClass.getDriver(), "Text comparison failed",
					"Failed: '" + text1 + "' not equals '" + text2 + "'");
		}
	}

	// Method to increment and validate the change
	public void validateNumberChange(By valueLocator, By actionButton, int expectedChange, String fieldName) {
		try {
			waitForElementToBeVisible(actionButton);
			applyBorder(actionButton, "green");

			int before = Integer.parseInt(getAttributeValue(valueLocator, "value"));

			clickJS(actionButton);
			wait.until(
					ExpectedConditions.attributeToBe(valueLocator, "value", String.valueOf(before + expectedChange)));

			int after = Integer.parseInt(getAttributeValue(valueLocator, "value"));

			logger.info(fieldName + " changed from " + before + " to " + after);
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Validate " + fieldName,
					fieldName + " changed from " + before + " to " + after);

		} catch (Exception e) {
			applyBorder(actionButton, "red");
			logger.error("Validation failed: " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Validation Failed - " + fieldName, e.getMessage());
		}
	}

	// Method to validate decrement after clicking minus button
	public void validateDecrement(By valueLocator, By minusButtonLocator, int expectedDecrease, String fieldName) {

		try {
			waitForElementToBeVisible(minusButtonLocator);
			applyBorder(minusButtonLocator, "green");

			int before = Integer.parseInt(getAttributeValue(valueLocator, "value"));

			clickJS(minusButtonLocator);

			wait.until(
					ExpectedConditions.attributeToBe(valueLocator, "value", String.valueOf(before - expectedDecrease)));

			int after = Integer.parseInt(getAttributeValue(valueLocator, "value"));

			logger.info(fieldName + " decreased from " + before + " to " + after);
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Validate Decrement - " + fieldName,
					fieldName + " decreased from " + before + " to " + after);

		} catch (Exception e) {
			applyBorder(minusButtonLocator, "red");
			logger.error("Decrement validation failed for " + fieldName + ": " + e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Decrement Validation Failed - " + fieldName,
					e.getMessage());
		}
	}

	// Method to refresh until element displayed
	public void refreshUntilElementDisplayed(By locator, int maxRefreshCount) {

		for (int i = 0; i < maxRefreshCount; i++) {

			logger.info("Checking element visibility. Attempt: " + (i + 1));

			if (isDisplayed(locator)) {
				logger.info("Element is displayed.");
				return;
			}

			logger.info("Element not displayed. Refreshing page...");
			driver.navigate().refresh();

			waitForPageLoad(30); // your existing method
		}

		throw new RuntimeException("Element not displayed after " + maxRefreshCount + " refresh attempts");
	}

	// Method to clear text
	public void clearText(By locator) {
		try {
			WebElement element = driver.findElement(locator);
			element.clear();
			logger.info("Text cleared successfully");
		} catch (Exception e) {
			logger.error("Unable to clear text: " + e.getMessage());
		}
	}

	// Method to getDomProperty
	public String getInputValueUsingDomProperty(By by) {
		try {
			WebElement element = driver.findElement(by);
			String value = element.getDomProperty("value");

			logger.info("Fetched input value: " + value);
			return value;

		} catch (Exception e) {
			logger.error("Unable to get input value: " + e.getMessage());
			return null;
		}
	}

	// Method to refresh page
	public void refreshPage() {
		try {

			logger.info("Refreshing the page");
			ExtentManager.logStep("Refreshing the current page");

			driver.navigate().refresh();

			waitForPageLoad(30);

			logger.info("Page refreshed successfully");
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Page refreshed", "Page refreshed successfully");

		} catch (Exception e) {

			logger.error("Failed to refresh the page: " + e.getMessage());

			ExtentManager.logFailure(BaseClass.getDriver(), "Page Refresh Failed", "Unable to refresh the page");
		}
	}

}
