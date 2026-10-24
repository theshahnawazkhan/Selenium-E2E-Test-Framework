package com.liskart.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.liskart.base.BaseClass;
import com.liskart.pages.AddressBookPage;
import com.liskart.pages.LisKartOrderPage;
import com.liskart.pages.LiskartCustomerLoginPage;
import com.liskart.pages.ProductDetailsPage;
import com.liskart.utilities.DataProviders;
import com.liskart.utilities.GraphQLOtpUtil;
import com.liskart.utilities.NavigationUtility;

public class B2CProductServiceabilityTest extends BaseClass {
	private LiskartCustomerLoginPage customerLoginPage;
	private LisKartOrderPage liskartOrderPage;
	private AddressBookPage addressBookPage;
	private ProductDetailsPage productDetailsPage;

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class, priority = 1)
	public void b2CProductOutOfStockTest(Map<String, String> data) throws Exception {

		// 1️⃣ Navigate to Customer URL
		NavigationUtility.goToCustomer();

		customerLoginPage = new LiskartCustomerLoginPage(getDriver());
		liskartOrderPage = new LisKartOrderPage(getDriver());
		addressBookPage = new AddressBookPage(getDriver());
		productDetailsPage = new ProductDetailsPage(getDriver());

		String phone = data.get("phone");

		// 3️⃣ Trigger OTP via UI
		customerLoginPage.clickOnLoginRegisterLink();
		customerLoginPage.enterPhoneNumber(phone);
		customerLoginPage.clickOnSendOtpBtn();

		// 4️⃣ Fetch OTP via GraphQL (API using browser cookies)
		String otp = GraphQLOtpUtil.fetchOtp(getDriver(), phone, "LOGIN");

		// 5️⃣ Login with OTP
		customerLoginPage.enterOtp(otp);
		customerLoginPage.clickOnLoginBtn();

		Thread.sleep(5000);
		Assert.assertTrue(customerLoginPage.verifyLogoDisplayed());

		// click on user link
		customerLoginPage.clickOnUser();

		// Click on Address Book link
		addressBookPage.clickOnAddressBookLink();
		Assert.assertTrue(addressBookPage.verifyAddressBookTextDisplayed());

		addressBookPage.clickOnAddAddressBtn();

		// Enter mandatory address fields
		addressBookPage.enterAddressFirstName(data.get("addressFirstName"));
		addressBookPage.enterAddressLastName(data.get("addressLastName"));
		addressBookPage.enterAddress1(data.get("address1"));
		addressBookPage.enteraddressPhone(data.get("phone"));
		addressBookPage.enterPincode(data.get("addressPinCode"));
		addressBookPage.selectState(data.get("addressState"));
		addressBookPage.selectCity(data.get("addressCity"));

		addressBookPage.clickOnDefaultShippingCheckBox();
		addressBookPage.clickOnSubmitBtn();

		Assert.assertTrue(addressBookPage.verifyDefaultIconDisplayedWithAddedAddress());

		addressBookPage.clickOnBacktn();

		// Search Product
		liskartOrderPage.searchSKU(data.get("sku"));
		liskartOrderPage.clickOnSearchBtn();
		liskartOrderPage.clickOnProduct();
		liskartOrderPage.verifyProductSKU(data.get("sku"));

		// Verify product out of stock
		Assert.assertTrue(productDetailsPage.verifyOutOfStockTextDisplayed());
		Assert.assertTrue(productDetailsPage.verifyNotifyMeBtnDisplayed());

	}

}
