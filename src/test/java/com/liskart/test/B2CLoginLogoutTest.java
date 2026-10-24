package com.liskart.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.liskart.base.BaseClass;
import com.liskart.pages.LiskartCustomerLoginPage;
import com.liskart.utilities.DataProviders;
import com.liskart.utilities.GraphQLOtpUtil;
import com.liskart.utilities.NavigationUtility;

public class B2CLoginLogoutTest extends BaseClass {

	private LiskartCustomerLoginPage customerLoginPage;

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class, priority = 1)
	public void b2CLoginLogoutTest(Map<String, String> data) throws InterruptedException {

		// 1️⃣ Navigate to Customer URL
		NavigationUtility.goToCustomer();

		// 2️⃣ Initialize page object AFTER navigation
		customerLoginPage = new LiskartCustomerLoginPage(getDriver());

		String phone = data.get("phone");

		// 3️⃣ Trigger OTP via UI
		customerLoginPage.clickOnLoginRegisterLink();
		customerLoginPage.enterPhoneNumber(phone);
		customerLoginPage.clickOnSendOtpBtn();

		// 4️⃣ Fetch OTP via GraphQL (API using browser cookies)
		//String otp = GraphQLOtpUtil.fetchOtp(getDriver(), phone, "LOGIN");

		String otp = data.get("otp");

		// 5️⃣ Login with OTP
		customerLoginPage.enterOtp(otp);
		customerLoginPage.clickOnLoginBtn();

		Thread.sleep(5000);
		Assert.assertTrue(customerLoginPage.verifyLogoDisplayed());

		// click on user link
		customerLoginPage.clickOnUser();

		// Click on sign Out button
		customerLoginPage.clickonSignOutLink();

		Assert.assertTrue(customerLoginPage.verifyLoginRegisterLinkDisplayed());
	}

}
