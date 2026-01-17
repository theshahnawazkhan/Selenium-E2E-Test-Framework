package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

	private static Map<Long, WebDriver> driverMap = new HashMap<>();

	public synchronized static ExtentReports getReporter() {
		if (extent == null) {

			String reportPath = System.getProperty("user.dir") + "/src/test/resources/ExtentReport/ExtentReport.html";

			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.DARK);

			extent = new ExtentReports();

			// Attach reporter
			extent.attachReporter(spark);

			// Adding system information
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
		}
		return extent;
	}

	public synchronized static ExtentTest startTest(String testName) {

		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;

	}

	// End a Test
	public static void endTest() {
		getReporter().flush();
	}

	// Get current Thread's Test
	public synchronized static ExtentTest getTest() {
		return test.get();
	}

	// Method to get the name of the current test
	public static String getTestName() {
		ExtentTest currentTest = getTest();
		if (currentTest != null) {
			return currentTest.getModel().getName();
		} else {
			return "No test is currently active for this thread";
		}
	}

	// Log a step
	public static void logStep(String logMessage) {

		getTest().info(logMessage);
	}

	// Log a step validation with screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String screenShotMessage) {
		getTest().pass(logMessage);
		// Screenshot method
		attachScreenshot(driver, screenShotMessage);

	}

	// Log a step validation for API
	public static void logStepValidationAPI(String logMessage) {
		getTest().pass(logMessage);
	}

	// Log a Failure
	public static void logFailure(WebDriver driver, String logMessage, String screenShotMessage) {
		String colorMessage = String.format("<span style = 'color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);

		// Screenshot method
		attachScreenshot(driver, screenShotMessage);
	}

	// Log a Failure for API
	public static void logFailureAPI(String logMessage) {
		String colorMessage = String.format("<span style = 'color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
	}

	// Log a skip
	public static void logSkip(String logMessage) {
		String colorMessage = String.format("<span style = 'color:orange;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
	}

	// Take screenshot with date and time
	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File srcFile = ts.getScreenshotAs(OutputType.FILE);
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());

		String screenshotDir = System.getProperty("user.dir") + "/src/test/resources/screenshots/";

		File directory = new File(screenshotDir);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		String destPath = screenshotDir + screenshotName + "_" + timeStamp + ".png";

		File FinalPath = new File(destPath);
		try {
			FileUtils.copyFile(srcFile, FinalPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Convert screenshot to Base64 for embedding in the report
		String base64Format = convertToBase64(FinalPath);
		return base64Format;
	}

	// Convert screenshot to Base64 format
	public static String convertToBase64(File screenshotFile) throws IOException {
		String base64Format = "";
		byte[] fileContent = FileUtils.readFileToByteArray(screenshotFile);
		base64Format = Base64.getEncoder().encodeToString(fileContent);
		return base64Format;

	}

	// Attach screenshot to report using Base64
	public synchronized static void attachScreenshot(WebDriver driver, String message) {
		String screenshotBase64;
		try {
			screenshotBase64 = takeScreenshot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder
					.createScreenCaptureFromBase64String(screenshotBase64).build());
		} catch (IOException e) {
			getTest().fail("Failed to attach screenshot:" + message);
			e.printStackTrace();
		}
	}

	// Register WebDriver for current Thread
	public static void registerDriver(WebDriver driver) {
		driverMap.put(Thread.currentThread().getId(), driver);
	}

}
