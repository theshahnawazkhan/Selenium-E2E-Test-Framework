package com.liskart.utilities;

import org.apache.logging.log4j.Logger;

import com.liskart.base.BaseClass;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class APIUtility {
	public static final Logger logger = BaseClass.logger;

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
	
	public static Response markShipmentDelivered(String awbNo) {

	    logger.info("Preparing payload to mark shipment delivered for AWB: " + awbNo);
	    ExtentManager.logStep("Marking shipment as Delivered for AWB: " + awbNo);

	    try {

	        String dateTime = java.time.LocalDateTime.now()
	                .format(java.time.format.DateTimeFormatter
	                        .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"));

	        String payload =
	                "{\n" +
	                "\"AWBNO\": \"" + awbNo + "\",\n" +
	                "\"reference_id\": \"SH001\",\n" +
	                "\"StatusCode\": \"DL\",\n" +
	                "\"Remarks\": \"Shipment has been delivered\",\n" +
	                "\"StatusDateTime\": \"" + dateTime + "\",\n" +
	                "\"CurrentLocation\": \"{\\\"latitude\\\": 18.9961767, \\\"longitude\\\": 72.8475614}\"\n" +
	                "}";

	        logger.info("Sending delivery update API request for AWB: " + awbNo);

	        Response response = RestAssured.given()
	                .header("Content-Type", "application/json")
	                .body(payload)
	                .post("https://preprod-console.liskart.com/rest/V1/fyndwebhook/delivered");

	        logger.info("API Response Status Code: " + response.getStatusCode());

	        ExtentManager.logStepWithScreenshot(
	                BaseClass.getDriver(),
	                "Shipment Delivery API",
	                "Shipment delivery API triggered successfully for AWB: " + awbNo +
	                        " | Status Code: " + response.getStatusCode()
	        );

	        return response;

	    } catch (Exception e) {

	        logger.error("Failed to mark shipment delivered for AWB: " + awbNo + " Error: " + e.getMessage());

	        ExtentManager.logFailure(
	                BaseClass.getDriver(),
	                "Shipment Delivery API Failed",
	                "Unable to mark shipment delivered for AWB: " + awbNo + " | Error: " + e.getMessage()
	        );

	        throw e;
	    }
	}
}
