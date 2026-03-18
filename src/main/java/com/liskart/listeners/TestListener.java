package com.liskart.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.liskart.base.BaseClass;
import com.liskart.utilities.ExtentManager;

public class TestListener implements ITestListener {

	@Override
	public void onTestStart(ITestResult result) {

		// Create Extent test only for the first invocation
		if (result.getMethod().getCurrentInvocationCount() == 0) {
			String testName = result.getMethod().getMethodName();

			ExtentManager.startTest(testName);
			ExtentManager.logStep("Test Started: " + testName);
		}
	}

	@Override
	public void onTestSuccess(ITestResult result) {

		String testName = result.getMethod().getMethodName();

		if (!result.getTestClass().getName().toLowerCase().contains("api")) {

			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Test Passes Successfully!",
					"Test End: " + testName + "_ ✔ Test Passed");

		} else {

			ExtentManager.logStepValidationAPI("Test End: " + testName + "_ ✔ Test Passed");

		}
	}

	@Override
	public void onTestFailure(ITestResult result) {

		String testName = result.getMethod().getMethodName();
		String failureMessage = result.getThrowable().getMessage();

		ExtentManager.logStep(failureMessage);

		if (!result.getTestClass().getName().toLowerCase().contains("api")) {

			ExtentManager.logFailure(BaseClass.getDriver(), "Test Failed!",
					"Test End: " + testName + "_ ❌ Test Failed");

		} else {

			ExtentManager.logFailureAPI("Test End: " + testName + "_ ❌ Test Failed");

		}
	}

	@Override
	public void onTestSkipped(ITestResult result) {

		// Ignore skipped retries (prevents duplicate logs)
		if (result.getThrowable() != null && result.getThrowable().toString().contains("Retry")) {
			return;
		}

		String testName = result.getMethod().getMethodName();
		ExtentManager.logSkip("Test Skipped! " + testName);
	}

	@Override
	public void onStart(ITestContext context) {

		// Initialize the extent report
		ExtentManager.getReporter();
	}

	@Override
	public void onFinish(ITestContext context) {

		// Flush the extent report
		ExtentManager.endTest();
	}
}