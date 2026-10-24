
package com.liskart.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.liskart.base.BaseClass;
import com.liskart.pages.LiskartCustomerLoginPage;
import com.liskart.pages.LiskartCustomerRegistrationPage;
import com.liskart.pages.LisKartOrderPage;
import com.liskart.pages.AddressBookPage;
import com.liskart.pages.CartPage;
import com.liskart.utilities.DataProviders;
import com.liskart.utilities.GraphQLOtpUtil;
import com.liskart.utilities.NavigationUtility;
import com.liskart.utilities.RetryAnalyzer;

public class B2CRegistrationAndFirstAddToCartTest extends BaseClass {

	private LiskartCustomerLoginPage customerLoginPage;
	private LiskartCustomerRegistrationPage liskartCustomerRegistrationPage;
	private LisKartOrderPage liskartOrderPage;
	private AddressBookPage addressBookPage;
	private CartPage cartPage;

	@Test(dataProvider = "registrationData", dataProviderClass = DataProviders.class, priority = 1, retryAnalyzer = RetryAnalyzer.class)
	public void b2CRegistrationAndFirstAddToCartTest(Map<String, String> data) throws InterruptedException {

		// 1️⃣ Navigate to Customer URL
		NavigationUtility.goToCustomer();

		// 2️⃣ Initialize page object AFTER navigation
		customerLoginPage = new LiskartCustomerLoginPage(getDriver());
		liskartCustomerRegistrationPage = new LiskartCustomerRegistrationPage(getDriver());
		liskartOrderPage = new LisKartOrderPage(getDriver());
		addressBookPage = new AddressBookPage(getDriver());
		cartPage = new CartPage(getDriver());

		customerLoginPage.clickOnLoginRegisterLink();
		liskartCustomerRegistrationPage.clickOnRegistrationLik();
		Assert.assertTrue(liskartCustomerRegistrationPage.verifyRegisterHeaderTextDisplayed());
		liskartCustomerRegistrationPage.enterFirstName(data.get("firstName"));
		liskartCustomerRegistrationPage.enterLastName(data.get("lastName"));
		String phone = LiskartCustomerRegistrationPage.generateRandomIndianPhoneNumber();
		customerLoginPage.enterPhoneNumber(phone);
		liskartCustomerRegistrationPage.selectCustomerProfile(data.get("customerProfileOption"));
		liskartCustomerRegistrationPage.clickOnTermAndConditionCheckBox();
		Assert.assertTrue(liskartCustomerRegistrationPage.verifyContinueBtnEnabled());
		liskartCustomerRegistrationPage.clickOnContinueBtn();
		// Fetch OTP via GraphQL (API using browser cookies)
		String otp = GraphQLOtpUtil.fetchOtp(getDriver(), phone, "REGISTER");
		customerLoginPage.enterOtp(otp);
		liskartCustomerRegistrationPage.clickOnOtpVerifyBtn();
		Assert.assertTrue(customerLoginPage.verifyUserLinkDisplayed());

		// After registration, search SKU and try to add to cart
		String sku = data.get("sku");
		liskartOrderPage.searchSKU(sku);
		liskartOrderPage.clickOnSearchBtn();
		liskartOrderPage.clickOnProduct();
		liskartOrderPage.addOrder(2);
		liskartOrderPage.clickOnAddToCartBtn();

		// User should be redirected to address page to add address first
		// Add address using data from Excel
		Thread.sleep(3000); // Wait for address page to load

		addressBookPage.enterAddressFirstName(data.get("addressFirstName"));
		addressBookPage.enterAddressLastName(data.get("addressLastName"));
		addressBookPage.enteraddressPhone(phone);
		addressBookPage.enterAddress1(data.get("address1"));
		addressBookPage.enterPincode(data.get("addressPinCode"));
		addressBookPage.selectState(data.get("addressState"));
		addressBookPage.selectCity(data.get("addressCity"));
		addressBookPage.clickOnSubmitBtn();

		// After adding address, try to add to cart again
		liskartOrderPage.addOrder(2);
		liskartOrderPage.clickOnAddToCartBtn();
		liskartOrderPage.clickOnCart();
		Assert.assertTrue(cartPage.isProductInCart(), "Product was not added to cart successfully.");
	}
}
