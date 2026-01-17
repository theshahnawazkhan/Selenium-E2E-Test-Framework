package com.orangehrm.test;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orangehrm.utilities.APIUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class APITest {

	@Test
	public void verifyGetUserAPI() {
		// step1: define API endpoint

		String endPoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtentManager.logStep("API EndPoint: " + endPoint);

		// Step2: sendGet Request
		ExtentManager.logStep("Sending get request to api");
		Response response = APIUtility.sendGetRequest(endPoint);

		// Step3: Validate status code
		ExtentManager.logStep("Validating statuscode");
		boolean isStatusCodeValid = APIUtility.validateStatusCode(response, 200);

		Assert.assertTrue(isStatusCodeValid, "Status code is not as expected");

		if (isStatusCodeValid) {
			ExtentManager.logStepValidationAPI("Status code validation passed!!");
		} else {
			ExtentManager.logFailureAPI("Status code validation failed!!");
		}

		// Step4: Validate username
		ExtentManager.logStep("Validating response body for username");
		String username = APIUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(username);

		Assert.assertTrue(isUserNameValid, "Username is not valid");
		if (isUserNameValid) {
			ExtentManager.logStepValidationAPI("Username validation passed!!");
		} else {
			ExtentManager.logFailureAPI("Username validation failed!!");
		}

		// Step4: Validate email
		ExtentManager.logStep("Validating response body for email");
		String userEmail = APIUtility.getJsonValue(response, "email");
		boolean isUserEmailValid = "Sincere@april.biz".equals(userEmail);

		Assert.assertTrue(isUserEmailValid, "Email is not valid");
		if (isUserEmailValid) {
			ExtentManager.logStepValidationAPI("Email validation passed!!");
		} else {
			ExtentManager.logFailureAPI("Email validation failed!!");
		}

	}
}
