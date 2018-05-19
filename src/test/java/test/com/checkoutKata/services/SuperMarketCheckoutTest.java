package test.com.checkoutKata.services;


import com.checkoutKata.model.GroceryItem;
import com.checkoutKata.services.SuperMarketCheckout;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SuperMarketCheckoutTest {

    @Test
    void canScanItems() {
        SuperMarketCheckout toTest = new SuperMarketCheckout();

        toTest.scanItem(new GroceryItem("A", new BigDecimal("5.0")).getStockKeepingUnit());
        toTest.scanItem(new GroceryItem("B", new BigDecimal("6.0")).getStockKeepingUnit());
        toTest.scanItem(new GroceryItem("A", new BigDecimal("5.0")).getStockKeepingUnit());

        assertEquals(new Integer(3), toTest.countScannedItems());
    }

}