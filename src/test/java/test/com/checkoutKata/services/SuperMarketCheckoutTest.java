package test.com.checkoutKata.services;


import com.checkoutKata.model.GroceryItem;
import com.checkoutKata.services.SuperMarketCheckout;
import com.sun.tools.javac.util.Assert;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class SuperMarketCheckoutTest {

    @Test
    void canScanItems() {
        SuperMarketCheckout toTest = new SuperMarketCheckout();

        toTest.scanItem(new GroceryItem("A", new BigDecimal("5.0")).getStoreKeepingUnit());
        toTest.scanItem(new GroceryItem("B", new BigDecimal("6.0")).getStoreKeepingUnit());
        toTest.scanItem(new GroceryItem("A", new BigDecimal("5.0")).getStoreKeepingUnit());

        Assert(toTest.countItems().size(), 3);
    }

}