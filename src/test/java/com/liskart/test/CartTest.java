package com.liskart.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.liskart.base.BaseClass;
import com.liskart.pages.CartPage;
import com.liskart.pages.LisKartOrderPage;
import com.liskart.pages.LiskartCustomerLoginPage;
import com.liskart.utilities.DataProviders;
import com.liskart.utilities.GraphQLOtpUtil;
import com.liskart.utilities.NavigationUtility;

public class CartTest extends BaseClass {

	private LiskartCustomerLoginPage customerLoginPage;
	private LisKartOrderPage liskartOrderPage;
	private CartPage cartPage;

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class, priority = 1)
	public void checkOutPageTest(Map<String, String> data) throws InterruptedException {

		// 1️⃣ Navigate to Customer URL
		NavigationUtility.goToCustomer();

		// 2️⃣ Initialize page object AFTER navigation
		customerLoginPage = new LiskartCustomerLoginPage(getDriver());
		liskartOrderPage = new LisKartOrderPage(getDriver());
		cartPage = new CartPage(getDriver());

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

		liskartOrderPage.searchSKU(data.get("sku"));
		liskartOrderPage.clickOnSearchBtn();
		liskartOrderPage.clickOnProduct();
		liskartOrderPage.verifyProductSKU(data.get("sku"));
		liskartOrderPage.addOrder(4);
		liskartOrderPage.clickOnAddToCartBtn();
		liskartOrderPage.clickOnCart();
		cartPage.increaseProductQuantity();
		cartPage.decreaseProductQuantity();
		cartPage.verifyTotalAmountWithMiniCartAmount(liskartOrderPage.getCartLocator());
		Thread.sleep(10000);
		cartPage.removeProductFromCart();
		cartPage.clickOnContinueShippingBtn();
		Assert.assertTrue(customerLoginPage.verifyLogoDisplayed());

	}

}
