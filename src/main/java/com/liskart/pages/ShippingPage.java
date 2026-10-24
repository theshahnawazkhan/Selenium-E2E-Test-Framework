package com.liskart.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import com.liskart.actiondriver.ActionDriver;
import com.liskart.base.BaseClass;
import com.liskart.utilities.ExtentManager;

public class ShippingPage {

    private ActionDriver actionDriver;

    // Define locator using By class
    private By priceLocator = By.xpath(
            " //div[span[contains(text(),'Price')]]/span[@class='ms-auto']");
    private By discountAmountLocator = By.xpath(
            " //div[span[contains(text(),'Price')]]/span[@class='ms-auto']/parent::div/following-sibling::div[1][span[contains(text(), 'Discount')]]/span[@class='ms-auto']");
    private By couponDiscountLocator = By.xpath(
            "//div[span[contains(text(),'Coupon Discount')]]/span[@class='ms-auto']");
    private By totalAmountLocator = By.xpath(
            "//span[contains(text(),'Total Amount')]/following-sibling::span | //div[span[contains(text(),'Total Amount')]]/span[last()] | //div[span[contains(text(),'Total Amount')]]/span[@class='ms-auto']");
    private By deliveryChargesLocator = By.xpath(
            "//div[span[contains(text(),'Delivery Charges')]]/span[@class='ms-auto']");
    private By nonSearviceablePincodeAddressInputLocator = By
            .xpath("//span[contains(text(),'700046')]/parent::label/preceding-sibling::input");
    private By addNewAddressBtnLocator = By.xpath("//button[contains(text(),'Add New Address')]");
    private By showMoreAddressBtnLocator = By.xpath("//button[contains(text(),'SHOW MORE ADDRESSES')]");
    
    private By deliveryNotAvailableToSelectedAddressMsgLocator = By
            .xpath("//span[contains(text(),'Delivery not available to the selected address')]");
    private By removeProductBtnForNonServiceableLocator = By.xpath(
            "//span[contains(text(),'Delivery not available to the selected address')]/parent::div/following-sibling::div/button");

    public ShippingPage(WebDriver driver) {
        this.actionDriver = BaseClass.getActionDriver();
    }

    /**
     * Utility to clean and parse currency string: trims, removes rupee sign, minus,
     * commas, and spaces.
     */
    private double cleanAmount(String text) {
        if (text == null)
            return 0.0;
        // Remove rupee sign, commas, spaces, ASCII minus (\u002D), Unicode minus
        // (\u2212)
        String raw = text;
        // Remove rupee sign, commas, spaces, minus (ASCII and Unicode)
        text = text.replaceAll("[₹,\\-−\\s]", "").trim(); // Character class: ₹ , - (ASCII) − (Unicode U+2212)
                                                          // whitespace
        text = text.replaceAll("[()]", "");
        if (text.isEmpty())
            return 0.0;
        try {
            double value = Double.parseDouble(text);
            ActionDriver.logger.info("[cleanAmount] Raw: '" + raw + "' Cleaned: '" + text + "' Parsed: " + value);
            return Math.abs(value);
        } catch (Exception e) {
            ActionDriver.logger.warn("[cleanAmount] Failed to parse: '" + text + "' from raw: '" + raw + "'");
            return 0.0;
        }
    }

    /**
     * Returns the price (before discounts) as double.
     */
    public double getPrice() {
        this.actionDriver.waitForElementToBeVisible(priceLocator);
        String priceText = actionDriver.getText(priceLocator);
        return cleanAmount(priceText);
    }

    /**
     * Returns the discount amount as positive double.
     */
    public double getDiscount() {
        this.actionDriver.waitForElementToBeVisible(discountAmountLocator);
        String discountText = actionDriver.getText(discountAmountLocator);
        return cleanAmount(discountText);
    }

    /**
     * Returns the total amount as double (after all discounts).
     */
    public double getTotalAmount() {
        String totalText = actionDriver.getText(totalAmountLocator);
        return cleanAmount(totalText);
    }

    /**
     * Returns the discount amount as positive double.
     */
    /**
     * Returns the delivery charges as positive double if present, else 0.0.
     */
    public double getDeliveryCharges() {
        try {
            if (BaseClass.getDriver().findElements(deliveryChargesLocator).size() > 0) {
                this.actionDriver.waitForElementToBeVisible(deliveryChargesLocator);
                String deliveryChargesText = actionDriver.getText(deliveryChargesLocator);
                double value = cleanAmount(deliveryChargesText);
                ActionDriver.logger.info("[getDeliveryCharges] Delivery charges present. Raw: '" + deliveryChargesText
                        + "' Cleaned: " + value);
                return value;
            } else {
                ActionDriver.logger.info("[getDeliveryCharges] Delivery charges not present. Returning 0.0");
            }
        } catch (Exception e) {
            ActionDriver.logger.warn("[getDeliveryCharges] Exception: " + e);
        }
        return 0.0;
    }

    /**
     * Returns the coupon discount as positive double.
     */
    public double getCouponDiscount() {
        try {
            if (BaseClass.getDriver().findElements(couponDiscountLocator).size() > 0) {
                this.actionDriver.waitForElementToBeVisible(couponDiscountLocator);
                String couponDiscountText = actionDriver.getText(couponDiscountLocator);
                double value = cleanAmount(couponDiscountText);
                ActionDriver.logger.info(
                        "[getCouponDiscount] Coupon present. Raw: '" + couponDiscountText + "' Cleaned: " + value);
                return value;
            } else {
                ActionDriver.logger.info("[getCouponDiscount] Coupon not present. Returning 0.0");
            }
        } catch (Exception e) {
            ActionDriver.logger.warn("[getCouponDiscount] Exception: " + e);
        }
        return 0.0;
    }

    /**
     * Validates cart summary: if coupon and/or delivery charges are present, total
     * = price - discount - coupon + deliveryCharges.
     * Returns true if calculation matches displayed total, false otherwise.
     */
    public boolean validateCartSummary() {
        double price = getPrice();
        ActionDriver.logger.info("[validateCartSummary] Price: " + price);
        double discount = getDiscount();
        ActionDriver.logger.info("[validateCartSummary] Discount: " + discount);
        double coupon = getCouponDiscount();
        double deliveryCharges = getDeliveryCharges();
        double total = getTotalAmount();
        ActionDriver.logger.info("[validateCartSummary] Coupon: " + coupon);
        ActionDriver.logger.info("[validateCartSummary] DeliveryCharges: " + deliveryCharges);
        ActionDriver.logger.info("[validateCartSummary] Displayed total: " + total);
        double expectedTotal = price - discount - coupon + deliveryCharges;
        expectedTotal = Math.round(expectedTotal * 100.0) / 100.0;
        total = Math.round(total * 100.0) / 100.0;
        String msg = "[CartSummary] Price: " + price + ", Discount: " + discount
                + ", Coupon: " + coupon
                + ", DeliveryCharges: " + deliveryCharges
                + ", ExpectedTotal: " + expectedTotal + ", DisplayedTotal: " + total;
        ActionDriver.logger.info(msg);
        ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "CartSummary Validation", msg);
        boolean result = Math.abs(expectedTotal - total) < 0.1;
        ActionDriver.logger.info("[validateCartSummary] Validation result: " + result);
        return result;
    }

    // Add method to select non-serviceable address
    public void selectNonserviceableAddress(AddressBookPage addressBookPage, String firstName, String lastName,
            String phone, String address1, String pincode, String state, String city) {
        try {
            // Only click 'Show More Addresses' if the element is present
            if (BaseClass.getDriver().findElements(showMoreAddressBtnLocator).size() > 0) {
                actionDriver.waitForElementToBeClickable(showMoreAddressBtnLocator);
                actionDriver.clickJS(showMoreAddressBtnLocator);
                ActionDriver.logger.info("[selectNonserviceableAddress] Clicked Show More Addresses");
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Show More Addresses",
                        "Clicked Show More Addresses");
            } else {
                ActionDriver.logger.info("[selectNonserviceableAddress] 'Show More Addresses' button not present, skipping click.");
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Show More Addresses Skipped",
                        "'Show More Addresses' button not present, skipping click.");
            }

            // Check if non-serviceable pincode input is present
            if (BaseClass.getDriver().findElements(nonSearviceablePincodeAddressInputLocator).size() > 0) {
                actionDriver.clickJS(nonSearviceablePincodeAddressInputLocator);
                ActionDriver.logger
                        .info("[selectNonserviceableAddress] Clicked on non-serviceable pincode address input");
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Non-serviceable Pincode",
                        "Clicked on non-serviceable pincode address input");
                return;
            } else {
                // Click Add New Address
                actionDriver.clickJS(addNewAddressBtnLocator);
                ActionDriver.logger.info("[selectNonserviceableAddress] Clicked Add New Address");
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Add New Address",
                        "Clicked Add New Address");

                // Fill address form using AddressBookPage
                addressBookPage.enterAddressFirstName(firstName);
                addressBookPage.enterAddressLastName(lastName);
                addressBookPage.enteraddressPhone(phone);
                addressBookPage.enterAddress1(address1);
                addressBookPage.enterPincode(pincode);
                addressBookPage.selectState(state);
                addressBookPage.selectCity(city);
                ActionDriver.logger.info("[selectNonserviceableAddress] Filled address form");
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Fill Address Form", "Filled address form");

                addressBookPage.clickOnSubmitBtn();
                ActionDriver.logger.info("[selectNonserviceableAddress] Clicked Submit on address form");
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Submit Address",
                        "Clicked Submit on address form");

                // After submit, check again for non-serviceable pincode input
                if (BaseClass.getDriver().findElements(nonSearviceablePincodeAddressInputLocator).size() > 0) {
                    actionDriver.click(nonSearviceablePincodeAddressInputLocator);
                    ActionDriver.logger.info(
                            "[selectNonserviceableAddress] Clicked on non-serviceable pincode address input after adding address");
                    ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Non-serviceable Pincode After Add",
                            "Clicked on non-serviceable pincode address input after adding address");
                } else {
                    ActionDriver.logger.warn(
                            "[selectNonserviceableAddress] Non-serviceable pincode address input not found after adding address");
                    ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Non-serviceable Pincode Not Found",
                            "Non-serviceable pincode address input not found after adding address");
                }
            }
        } catch (Exception e) {
            ActionDriver.logger.error("[selectNonserviceableAddress] Exception: " + e.getMessage(), e);
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Exception in selectNonserviceableAddress",
                    e.getMessage());
        }
    }

    /**
     * Checks if the 'Delivery not available to the selected address' message is
     * displayed.
     */
    public boolean isDeliveryNotAvailableMsgDisplayed() {
        boolean displayed = BaseClass.getDriver().findElements(deliveryNotAvailableToSelectedAddressMsgLocator)
                .size() > 0;
        ActionDriver.logger.info("[isDeliveryNotAvailableMsgDisplayed] Displayed: " + displayed);
        return displayed;
    }

    /**
     * Clicks the remove button for non-serviceable product(s) until the cart is
     * empty message is displayed.
     * Uses ActionDriver for all actions and logs each step.
     */
    public void removeAllNonServiceableProductsUntilCartEmpty() {
        try {
            while (isDeliveryNotAvailableMsgDisplayed()) {
                actionDriver.waitForElementToBeClickable(removeProductBtnForNonServiceableLocator);
                actionDriver.clickJS(removeProductBtnForNonServiceableLocator);
                ActionDriver.logger.info(
                        "[removeAllNonServiceableProductsUntilCartEmpty] Clicked remove button for non-serviceable product");
                ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Remove Non-Serviceable Product",
                        "Clicked remove button for non-serviceable product");
                // Optionally, add a short wait if UI needs time to update
                Thread.sleep(1000);
            }
            ActionDriver.logger
                    .info("[removeAllNonServiceableProductsUntilCartEmpty] No more non-serviceable products in cart.");
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Cart Empty",
                    "No more non-serviceable products in cart.");
        } catch (Exception e) {
            ActionDriver.logger.error("[removeAllNonServiceableProductsUntilCartEmpty] Exception: " + e.getMessage(),
                    e);
            ExtentManager.logStepWithScreenshot(BaseClass.getDriver(),
                    "Exception in removeAllNonServiceableProductsUntilCartEmpty", e.getMessage());
        }
    }

    // Validate order summary details like price, discount, coupon discount, and total (without delivery charges)

    /**
     * Validates order view summary: total = price - discount - coupon.
     * Delivery charges are NOT included in this calculation.
     * Returns true if calculation matches displayed total, false otherwise.
     */
    public boolean validateOrderViewSummary() {
        double price = getPrice();
        ActionDriver.logger.info("[validateOrderViewSummary] Price: " + price);
        double discount = getDiscount();
        ActionDriver.logger.info("[validateOrderViewSummary] Discount: " + discount);
        double coupon = getCouponDiscount();
        ActionDriver.logger.info("[validateOrderViewSummary] Coupon: " + coupon);
        double total = getTotalAmount();
        ActionDriver.logger.info("[validateOrderViewSummary] Displayed total: " + total);
        double expectedTotal = price - discount - coupon;
        expectedTotal = Math.round(expectedTotal * 100.0) / 100.0;
        total = Math.round(total * 100.0) / 100.0;
        String msg = "[OrderViewSummary] Price: " + price + ", Discount: " + discount
                + ", Coupon: " + coupon
                + ", ExpectedTotal: " + expectedTotal + ", DisplayedTotal: " + total;
        ActionDriver.logger.info(msg);
        ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "OrderViewSummary Validation", msg);
        boolean result = Math.abs(expectedTotal - total) < 0.1;
        ActionDriver.logger.info("[validateOrderViewSummary] Validation result: " + result);
        return result;
    }

}
