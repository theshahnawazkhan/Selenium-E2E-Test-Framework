package com.liskart.test;

import java.util.List;
import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.liskart.base.BaseClass;
import com.liskart.pages.CartPage;
import com.liskart.pages.LisKartOrderPage;
import com.liskart.pages.ShippingPage;
import com.liskart.pages.LiskartCustomerLoginPage;
import com.liskart.utilities.DataProviders;
import com.liskart.utilities.NavigationUtility;
import com.liskart.utilities.SkuJsonUtil;

public class BulkSkuCartTest extends BaseClass {

    private LiskartCustomerLoginPage customerLoginPage;
    private LisKartOrderPage liskartOrderPage;
    private CartPage cartPage;
    private ShippingPage shippingPage;

    @Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class, priority = 1)
    public void addRemoveBulkSkusAndValidateCart(Map<String, String> data) throws InterruptedException {
        // 1️⃣ Navigate to Customer URL
        NavigationUtility.goToCustomer();

        // 2️⃣ Initialize page objects
        customerLoginPage = new LiskartCustomerLoginPage(getDriver());
        liskartOrderPage = new LisKartOrderPage(getDriver());
        cartPage = new CartPage(getDriver());
        shippingPage = new ShippingPage(getDriver());
        String phone = data.get("phone");
        String otp = data.get("otp");

        // 3️⃣ Login (replace with your login logic as needed)
        customerLoginPage.clickOnLoginRegisterLink();
        customerLoginPage.enterPhoneNumber(phone); // Replace with valid phone
        customerLoginPage.clickOnSendOtpBtn();
        customerLoginPage.enterOtp(otp); // Replace with valid OTP
        customerLoginPage.clickOnLoginBtn();
        Thread.sleep(3000);
        Assert.assertTrue(customerLoginPage.verifyLogoDisplayed());

        // 4️⃣ Add first 10 SKUs to cart
        List<String> allSkus = SkuJsonUtil.getAllSkus();
        int limit = Math.min(3, allSkus.size());
        for (int i = 0; i < limit; i++) {
            String sku = allSkus.get(i);
            Thread.sleep(2000);
            liskartOrderPage.clearSearchField();
            Thread.sleep(5000);
            liskartOrderPage.searchSKU(sku);
            liskartOrderPage.clickOnSearchBtn();
            liskartOrderPage.clickOnProduct();
            liskartOrderPage.addOrder(1);
            liskartOrderPage.clickOnAddToCartBtn();
        }
        liskartOrderPage.clickOnCart();
        // 5️⃣ Validate cart total after adding
        Assert.assertTrue(
                cartPage.verifyTotalAmountWithMiniCartAmount(liskartOrderPage.getCartLocator()),
                "Cart Amount  is not matching with mini cart amount after adding SKUs");

        // 6️⃣ Remove all SKUs from cart
        cartPage.removeProductFromCart();
        cartPage.clickOnContinueShippingBtn();
        Assert.assertTrue(customerLoginPage.verifyLogoDisplayed());

        // 7️⃣ Add the same 10 SKUs again
        for (int i = 0; i < limit; i++) {
            String sku = allSkus.get(i);
            Thread.sleep(2000);
            liskartOrderPage.clearSearchField();
            Thread.sleep(5000);
            liskartOrderPage.searchSKU(sku);
            liskartOrderPage.clickOnSearchBtn();
            liskartOrderPage.clickOnProduct();
            liskartOrderPage.addOrder(1);
            liskartOrderPage.clickOnAddToCartBtn();

        }
        liskartOrderPage.clickOnCart();
        // 8️⃣ Validate cart total after re-adding
        Assert.assertTrue(
                cartPage.verifyTotalAmountWithMiniCartAmount(liskartOrderPage.getCartLocator()),
                "Cart Amount  is not matching with mini cart amount after re-adding SKUs");

        // 9️⃣ Go to shipping page and check summary
        // (Assuming you have a method to proceed to shipping)
        // liskartOrderPage.clickOnProceedToCheckoOut();
        // Assert.assertTrue(shippingPage.validateCartSummary(), "Shipping summary
        // incorrect");
    }
}
