package com.liskart.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.liskart.base.BaseClass;
import com.liskart.pages.AdminPage;
import com.liskart.pages.LisKartOrderPage;
import com.liskart.pages.LiskartCustomerLoginPage;
import com.liskart.pages.SellerPage;
import com.liskart.pages.ShippingPage;
import com.liskart.utilities.DataProviders;
import com.liskart.utilities.GraphQLOtpUtil;
import com.liskart.utilities.NavigationUtility;

public class LisKartE2EOrderTest extends BaseClass {

	private LiskartCustomerLoginPage customerLoginPage;
	private LisKartOrderPage liskartOrderPage;
	private AdminPage adminPage;
	private SellerPage sellerPage;
	private ShippingPage shippingPage;
	String thankText = "Thank you for purchasing our product";

	@Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class, priority = 1)
	public void verifyE2EOrderTest(Map<String, String> data) throws Exception {
		adminPage = new AdminPage(getDriver());
		sellerPage = new SellerPage(getDriver());
		// customerLoginPage = new LiskartCustomerLoginPage(getDriver());
		// liskartOrderPage = new LisKartOrderPage(getDriver());

		// 1️⃣ Navigate to Customer URL
		NavigationUtility.goToCustomer();

		// 2️⃣ Initialize page object AFTER navigation
		customerLoginPage = new LiskartCustomerLoginPage(getDriver());
		liskartOrderPage = new LisKartOrderPage(getDriver());
		adminPage = new AdminPage(getDriver());
		shippingPage = new ShippingPage(getDriver());

		String phone = data.get("phone");
		String otp = data.get("otp");

		// 3️⃣ Trigger OTP via UI
		customerLoginPage.clickOnLoginRegisterLink();
		customerLoginPage.enterPhoneNumber(phone);
		customerLoginPage.clickOnSendOtpBtn();

		// 4️⃣ Fetch OTP via GraphQL (API using browser cookies)
		//String otp = GraphQLOtpUtil.fetchOtp(getDriver(), phone, "LOGIN");

		// 5️⃣ Login with OTP
		customerLoginPage.enterOtp(otp);
		customerLoginPage.clickOnLoginBtn();

		Thread.sleep(5000);
		Assert.assertTrue(customerLoginPage.verifyLogoDisplayed());

		liskartOrderPage.searchSKU(data.get("sku"));
		liskartOrderPage.clickOnSearchBtn();
		liskartOrderPage.clickOnProduct();
		liskartOrderPage.verifyProductSKU(data.get("sku"));
		liskartOrderPage.addOrder(5);
		liskartOrderPage.clickOnAddToCartBtn();
		liskartOrderPage.clickOnCart();
		liskartOrderPage.clickOnProceedToCheckoOut();
		Thread.sleep(10000);
		// liskartOrderPage.clickOnSelectCouponLink();
		// liskartOrderPage.clickOnCoupon();
		// liskartOrderPage.clickOnApplyCouponBtn();
		liskartOrderPage.clickOnCashOnDeliveryInputBox();
		liskartOrderPage.clickOnPlaceOrderBtn();
		Assert.assertTrue(liskartOrderPage.verifyPlacedThankYouMsg(thankText));
		liskartOrderPage.clickOnViewOrdersBtn();
		String orderId = liskartOrderPage.getOrderId();
		System.out.println(orderId);
		liskartOrderPage.clickOnOrderViewBtn();
		Assert.assertTrue(shippingPage.validateOrderViewSummary(), "Order view summary validation failed");

		// 1️⃣ Navigate to pre-prod Admin
		NavigationUtility.goToAdmin();
		adminPage.enterAdminUserName(data.get("adminUserName"));
		adminPage.enterAdminPassword(data.get("adminPassword"));
		adminPage.clickOnAdminSignInBtn();
		adminPage.closeModelPop();
		Assert.assertTrue(adminPage.verifyAdminDashoadTitleDisplayed());
		adminPage.clickOnSalesTab();
		adminPage.clickOnOrdersLink();
		adminPage.clearOrderInputTextBox();
		adminPage.enterOrderID(orderId);
		adminPage.clickOnOrderSearchBtn();
		adminPage.verifyOrderStatusDisplayed();
		adminPage.verifyApprovalStatusDisplayed();
		adminPage.clickOnView();
		adminPage.refreshUntilElementDisplayed();
		Assert.assertTrue(adminPage.verifySellerLinkDisplayed());
		Assert.assertTrue(adminPage.verifyOrderCalculations(),
				"Order calculations (subtotal, tax, row total, grand total) validation failed in Admin panel");
		adminPage.compareGrandTotalAndTotalDuePrice();
		adminPage.clickOnSellerLink();
		adminPage.refreshUntilSellerInformationDisplayed();
		adminPage.clickOnSellerAccountInformationLink();
		adminPage.scrollToSellerPhoneInputBox();
		String sellerPhoneNumber = adminPage.getSellerPhoneNumber();
		System.out.println(sellerPhoneNumber);

		// Navigate to preprod seller
		NavigationUtility.goToSeller();
		Thread.sleep(5000);
		customerLoginPage.clickOnUser();
		customerLoginPage.clickonSignOutLink();
		sellerPage.clickOnSellerLoginLink();
		Assert.assertTrue(sellerPage.verifySellerLoginHeaderDisplayed());
		customerLoginPage.enterPhoneNumber(sellerPhoneNumber);
		customerLoginPage.clickOnSendOtpBtn();

		// 4️⃣ Fetch OTP via GraphQL (API using browser cookies)
		String sellerOtp = GraphQLOtpUtil.fetchOtp(getDriver(), sellerPhoneNumber, "SELLER_LOGIN");

		// 5️⃣ Login with OTP
		customerLoginPage.enterOtp(sellerOtp);
		sellerPage.clickonSellerLoginBtn();
		Thread.sleep(5000);
		Assert.assertTrue(customerLoginPage.verifyLogoDisplayed());
		sellerPage.clickOnSellerOrderHistory();
		sellerPage.verifyOrderHistoryHeaderTextDisplayed();
		sellerPage.enterOrderId(orderId);
		sellerPage.clickOnSubmitBtn();
		String sellerOrderId = liskartOrderPage.getOrderId();
		Assert.assertEquals(sellerOrderId, orderId, "Order Id is not equal");
		Assert.assertEquals(sellerPage.orderStatus(), "Pending", "Order status is not valid");
		sellerPage.clickOnAcceptLink();
		Thread.sleep(15000);
		Assert.assertEquals(sellerPage.orderStatus(), "Order Accepted", "Order status is not valid");
		sellerPage.clickOnGenerateInvoiceLink();
		sellerPage.clickOnModalGenerateInvoiceBtn();
		sellerPage.clickOnCloseBtn();
		Thread.sleep(5000);
		Assert.assertEquals(sellerPage.orderStatus(), "Invoiced", "Order status is not valid");
		sellerPage.clickOGenerateShippingLabelLink();
		Thread.sleep(5000);
		Assert.assertEquals(sellerPage.orderStatus(), "Complete", "Order status is not valid");
		sellerPage.clickOnDownloadShippingLabelLink();
		Thread.sleep(5000);
		sellerPage.downloadShippingLabelAndDeliveredOrder();
		sellerPage.refreshSellerPageAfterDelivery();
		Thread.sleep(5000);
		Assert.assertEquals(sellerPage.orderStatus(), "Delivered", "Order status is not valid");

	}
}
