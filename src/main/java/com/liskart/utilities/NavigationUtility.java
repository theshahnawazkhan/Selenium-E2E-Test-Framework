package com.liskart.utilities;
import com.liskart.base.BaseClass;

public class NavigationUtility {

    public static void goToCustomer() {
        BaseClass.getDriver()
            .get(BaseClass.getProp().getProperty("customer.url"));
    }

    public static void goToAdmin() {
        BaseClass.getDriver()
            .get(BaseClass.getProp().getProperty("admin.url"));
    }

    public static void goToSeller() {
        BaseClass.getDriver()
            .get(BaseClass.getProp().getProperty("seller.url"));
    }
}
