package com.liskart.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.liskart.actiondriver.ActionDriver;
import com.liskart.utilities.ExtentManager;
import com.liskart.utilities.FileUtility;
import com.liskart.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;

	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actionDriver = new ThreadLocal<>();

	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);

	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);

	/* ================= LOAD CONFIG ================= */

	@BeforeSuite
	public void loadConfig() throws IOException {
		prop = new Properties();
		FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
		prop.load(fis);
	}

	/* ================= LAUNCH BROWSER ================= */

	protected void launchBrowser() {

		String browser = prop.getProperty("browser");
		WebDriver localDriver;

		if (browser.equalsIgnoreCase("chrome")) {

			ChromeOptions options = new ChromeOptions();

			// 🔥 ENABLE NETWORK / GRAPHQL LOG CAPTURE
			options.setCapability("goog:loggingPrefs", Map.of("performance", "ALL"));
			// 🔽 ADD DOWNLOAD SETTINGS (SAFE ADDITION)
			Map<String, Object> prefs = new HashMap<>();

			String downloadPath = System.getProperty("user.dir") + File.separator + "downloads";

			prefs.put("download.default_directory", downloadPath);
			prefs.put("download.prompt_for_download", false);
			prefs.put("plugins.always_open_pdf_externally", true);

			options.setExperimentalOption("prefs", prefs);

			localDriver = new ChromeDriver(options);

		} else if (browser.equalsIgnoreCase("firefox")) {

			localDriver = new FirefoxDriver();

		} else if (browser.equalsIgnoreCase("edge")) {

			localDriver = new EdgeDriver();

		} else {
			throw new IllegalArgumentException("Browser Not Supported: " + browser);
		}

		driver.set(localDriver);
		ExtentManager.registerDriver(localDriver);
	}

	/* ================= CONFIGURE BROWSER ================= */

	public void configureBrowser() {
		int implicitWait = Integer.parseInt(prop.getProperty("implicitWait"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitWait));
		getDriver().manage().window().maximize();
	}

	/* ================= TEST SETUP ================= */

	@BeforeMethod
	public void setup(Method method) {

		logger.info("Starting Test: " + method.getName());
		//ExtentManager.startTest(method.getName());

		String downloadPath = System.getProperty("user.dir") + File.separator + "downloads";

		FileUtility.clearDownloadsFolder(downloadPath);

		launchBrowser();
		configureBrowser();

		actionDriver.set(new ActionDriver(getDriver()));
	}

	/* ================= TEARDOWN ================= */

	@AfterMethod(alwaysRun = true)
	public void tearDown() {

		try {
			if (getDriver() != null) {
				getDriver().quit();
			}
		} catch (Exception e) {
			logger.error("Unable to quit driver: " + e.getMessage());
		}

		driver.remove();
		actionDriver.remove();
	}

	@AfterSuite
	public void afterSuite() {
		ExtentManager.endTest();
	}

	/* ================= GETTERS ================= */

	public static WebDriver getDriver() {
		return driver.get();
	}

	public static ActionDriver getActionDriver() {
		return actionDriver.get();
	}

	public static Properties getProp() {
		return prop;
	}

	/* ================= UTIL ================= */

	public void staticWait(int seconds) {
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}
}
