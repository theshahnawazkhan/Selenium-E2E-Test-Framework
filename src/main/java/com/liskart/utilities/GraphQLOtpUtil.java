package com.liskart.utilities;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.useRelaxedHTTPSValidation;

import org.openqa.selenium.WebDriver;

import io.restassured.response.Response;

public class GraphQLOtpUtil {

    private static final String GRAPHQL_URL = "https://preprod-console.liskart.com/graphql";

    public static String fetchOtp(WebDriver driver, String phone, String otpAction) {

        useRelaxedHTTPSValidation();

        String cookieHeader = driver.manage().getCookies().stream()
                .map(c -> c.getName() + "=" + c.getValue())
                .reduce((a, b) -> a + "; " + b)
                .orElse("");

        String payload = buildOtpPayload(phone, otpAction);

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", cookieHeader)
                .body(payload)
            .when()
                .post(GRAPHQL_URL)
            .then()
                .statusCode(200)
                .extract()
                .response();

        String otp = response.jsonPath().getString("data.sendOtp.otp");

        if (otp == null || otp.isEmpty()) {
            throw new RuntimeException("OTP not returned by backend");
        }

        System.out.println("✅ OTP captured (" + otpAction + "): " + otp);
        return otp.replaceAll("\\D", "");
    }

    private static String buildOtpPayload(String phone, String otpAction) {
        return """
        {
          "operationName": "SendOtp",
          "variables": {
            "phoneNumber": "%s",
            "otpAction": "%s"
          },
          "query": "mutation SendOtp($phoneNumber: String!, $otpAction: String!) { sendOtp(input: { phone_number: $phoneNumber, otp_action: $otpAction }) { otp __typename } }"
        }
        """.formatted(phone, otpAction);
    }
}
