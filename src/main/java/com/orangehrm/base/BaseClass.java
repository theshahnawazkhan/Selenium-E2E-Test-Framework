package com.orangehrm.base;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;

	// ThreadLocal objects
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();
	
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	
	public SoftAssert getSoftAssert() {
		return softAssert.get();	
	}

	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	@BeforeSuite
	public void loadConfig() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		prop.load(fis);

		// start the Extent report
		//ExtentManager.getReporter();
	}

	// Launch browser per thread
	private void launchBrowser() {
		String browser = prop.getProperty("browser");

		WebDriver localDriver;

		if (browser.equalsIgnoreCase("chrome")) {
			localDriver = new ChromeDriver();
		} else if (browser.equalsIgnoreCase("firefox")) {
			localDriver = new FirefoxDriver();
		} else if (browser.equalsIgnoreCase("edge")) {
			localDriver = new EdgeDriver();
		} else {
			throw new IllegalArgumentException("Browser Not Supported: " + browser);
		}

		driver.set(localDriver); // set driver for current thread
		ExtentManager.registerDriver(localDriver);
	}

	// Configure per-thread browser
	public void configureBrowser() {
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));

		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		getDriver().manage().window().maximize();

		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {
			logger.error("Failed to navigate to URL: " + e.getMessage());
		}
	}

	@BeforeMethod
	public void setup() {
		logger.info("Setting up webdriver for: " + this.getClass().getSimpleName() + " | Thread: "
				+ Thread.currentThread().getId());

		launchBrowser();
		configureBrowser();
		staticWait(2);

		// ActionDriver per thread
		actionDriver.set(new ActionDriver(getDriver()));
		logger.info("ActionDriver created | Thread: " + Thread.currentThread().getId());
	}

	@AfterMethod
	public synchronized void tearDown() {
		try {
			if (getDriver() != null) {
				getDriver().quit();
			}
		} catch (Exception e) {
			logger.error("Unable to quit driver: " + e.getMessage());
		}

		// lean ThreadLocal
		driver.remove();
		actionDriver.remove();
		logger.info("Driver closed | Thread: " + Thread.currentThread().getId());
		//ExtentManager.endTest();
	}

	// Getter methods (important for POM & utilities)
	public static WebDriver getDriver() {
		if (driver.get() == null) {
			throw new IllegalStateException("WebDriver is not initialized for this thread");
		}
		return driver.get();
	}

	public static ActionDriver getActionDriver() {
		if (actionDriver.get() == null) {
			throw new IllegalStateException("ActionDriver is not initialized for this thread");
		}
		return actionDriver.get();
	}

	public static Properties getProp() {
		return prop;
	}

	// Static wait
	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}
