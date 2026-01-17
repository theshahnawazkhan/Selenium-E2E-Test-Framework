package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIUtility {

	public static Response sendGetRequest(String endPoint) {
		return RestAssured.get(endPoint);
	}

	public static Response sendPostRequest(String endPoint, String payLoad) {
		return RestAssured.given().header("Content-Type", "application/json").body(payLoad).post(endPoint);
	}

	public static boolean validateStatusCode(Response response, int statusCode) {
		return response.getStatusCode() == statusCode;
	}

	public static String getJsonValue(Response response, String jsonPath) {
		return response.jsonPath().getString(jsonPath);
	}
}
