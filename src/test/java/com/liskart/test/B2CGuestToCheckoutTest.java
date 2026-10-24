package com.liskart.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.liskart.base.BaseClass;
import com.liskart.pages.CartPage;
import com.liskart.pages.LisKartOrderPage;
import com.liskart.pages.LiskartCustomerLoginPage;
import com.liskart.pages.ShippingPage;
import com.liskart.pages.AddressBookPage;
import com.liskart.utilities.DataProviders;
import com.liskart.utilities.NavigationUtility;

public class B2CGuestToCheckoutTest extends BaseClass {

        private LiskartCustomerLoginPage customerLoginPage;
        private LisKartOrderPage liskartOrderPage;
        private CartPage cartPage;
        private ShippingPage shippingPage;
        private AddressBookPage addressBookPage;

        @Test(dataProvider = "validLoginData", dataProviderClass = DataProviders.class)
        public void b2CGuestSearchAddToCartAndCheckout(Map<String, String> data) throws Exception {
                // 1️⃣ Navigate to Customer URL
                NavigationUtility.goToCustomer();

                liskartOrderPage = new LisKartOrderPage(getDriver());
                cartPage = new CartPage(getDriver());
                customerLoginPage = new LiskartCustomerLoginPage(getDriver());
                shippingPage = new ShippingPage(getDriver());
                addressBookPage = new AddressBookPage(getDriver());

                // 2. Search product (SKU from data or JSON)
                String sku = data.get("sku");

                liskartOrderPage.searchSKU(sku);
                liskartOrderPage.clickOnSearchBtn();
                liskartOrderPage.clickOnProduct();
                liskartOrderPage.addOrder(2); // or as needed
                liskartOrderPage.clickOnAddToCartBtn();

                // 3. Should be navigated to login page
                customerLoginPage.enterPhoneNumber(data.get("phone"));
                customerLoginPage.clickOnSendOtpBtn();
                customerLoginPage.enterOtp(data.get("otp"));
                customerLoginPage.clickOnLoginBtn();

                // 4. Go to cart page
                // liskartOrderPage.clickOnCart();

                // 5. Remove any product in cart
                // cartPage.removeProductFromCart();

                // 6. Search SKU again, increase quantity, add to cart
                // liskartOrderPage.searchSKU(sku);
                // liskartOrderPage.clickOnSearchBtn();
                // liskartOrderPage.clickOnProduct();
                liskartOrderPage.addOrder(4);
                liskartOrderPage.clickOnAddToCartBtn();
                Thread.sleep(5000);
                liskartOrderPage.clickOnCart();

                // 7. Compare total amount and mini cart amount
                cartPage.verifyTotalAmountWithMiniCartAmount(liskartOrderPage.getCartLocator());
                Assert.assertTrue(cartPage.verifyTotalAmountWithMiniCartAmount(liskartOrderPage.getCartLocator()),
                                "Total and mini cart amounts do not match");

                // 8. Click on place order
                liskartOrderPage.clickOnProceedToCheckoOut();

                // 9. Validate cart summary (price, discount, coupon, total)
                Assert.assertTrue(shippingPage.validateCartSummary(),
                                "Cart summary calculation does not match displayed total");
                liskartOrderPage.applyCouponIfNotPresent();
                Assert.assertTrue(shippingPage.validateCartSummary(),
                                "Cart summary after coupon does not match expected total");

                // 9. Select non-serviceable address using data from Excel
                shippingPage.selectNonserviceableAddress(addressBookPage, data.get("addressFirstName"),
                                data.get("addressLastName"),
                                data.get("phone"),
                                data.get("address1"),
                                data.get("addressPinCode"),
                                data.get("addressState"),
                                data.get("addressCity"));

                // 10. Remove all non-serviceable products until cart is empty
                shippingPage.removeAllNonServiceableProductsUntilCartEmpty();

        }
}
