package com.liskart.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.liskart.base.BaseClass;
import com.liskart.pages.LiskartCustomerLoginPage;
import com.liskart.pages.LiskartCustomerRegistrationPage;
import com.liskart.pages.SellerPage;
import com.liskart.utilities.DataProviders;
import com.liskart.utilities.GraphQLOtpUtil;
import com.liskart.utilities.NavigationUtility;
import com.liskart.utilities.RetryAnalyzer;

public class B2BRegistrationTest extends BaseClass {

    private LiskartCustomerLoginPage customerLoginPage;
    private LiskartCustomerRegistrationPage liskartCustomerRegistrationPage;
    private SellerPage sellerPage;

    @Test(dataProvider = "registrationData", dataProviderClass = DataProviders.class, priority = 1, retryAnalyzer = RetryAnalyzer.class)
    public void b2BRegistrationTest(Map<String, String> data) throws InterruptedException {

        // 1️⃣ Navigate to Customer URL
        NavigationUtility.goToCustomer();

        // 2️⃣ Initialize page object AFTER navigation
        customerLoginPage = new LiskartCustomerLoginPage(getDriver());
        liskartCustomerRegistrationPage = new LiskartCustomerRegistrationPage(getDriver());
        sellerPage = new SellerPage(getDriver());

        customerLoginPage.clickOnLoginRegisterLink();
        liskartCustomerRegistrationPage.clickOnRegistrationLik();
        Assert.assertTrue(liskartCustomerRegistrationPage.verifyRegisterHeaderTextDisplayed());
        liskartCustomerRegistrationPage.enterFirstName(data.get("firstName"));
        liskartCustomerRegistrationPage.enterLastName(data.get("lastName"));
        String phone = LiskartCustomerRegistrationPage.generateRandomIndianPhoneNumber();
        customerLoginPage.enterPhoneNumber(phone);
        liskartCustomerRegistrationPage.selectCustomerProfile(data.get("customerProfileOption"));
        // Enter GST number before checkbox
        sellerPage.enterSellerGstNumber(data.get("gstNumber"));
        liskartCustomerRegistrationPage.clickOnTermAndConditionCheckBox();
        Assert.assertTrue(liskartCustomerRegistrationPage.verifyContinueBtnEnabled());
        liskartCustomerRegistrationPage.clickOnContinueBtn();
        // After continue, verify registration header from seller page
        Assert.assertTrue(sellerPage.verifyYourGstDetailsHeaderTextDisplayed());
        // Click seller registration button before OTP
        sellerPage.clickOnSellerRegisterBtn();
        // Fetch OTP via GraphQL (API using browser cookies)

        String otp = GraphQLOtpUtil.fetchOtp(getDriver(), phone, "REGISTER");
        customerLoginPage.enterOtp(otp);
        //liskartCustomerRegistrationPage.clickOnOtpVerifyBtn();
        //Assert.assertTrue(customerLoginPage.verifyUserLinkDisplayed());
    }
}
