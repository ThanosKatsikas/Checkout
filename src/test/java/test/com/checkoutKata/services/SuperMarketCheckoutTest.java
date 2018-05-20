package test.com.checkoutKata.services;


import com.checkoutKata.model.GroceryItem;
import com.checkoutKata.repository.SuperMarketWarehouse;
import com.checkoutKata.services.SuperMarketCheckout;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuperMarketCheckoutTest {

    @Test
    void canScanItems() {
        SuperMarketCheckout toTest = new SuperMarketCheckout();

        toTest.scanItem(new GroceryItem("A", new BigDecimal("5.0")).getStockKeepingUnit());
        toTest.scanItem(new GroceryItem("B", new BigDecimal("6.0")).getStockKeepingUnit());
        toTest.scanItem(new GroceryItem("A", new BigDecimal("5.0")).getStockKeepingUnit());

        assertEquals(new Integer(3), toTest.countScannedItems());
    }

    @Test
    void shouldReturnZeroIfThereAreNoItemsScanned() {
        SuperMarketCheckout toTest = new SuperMarketCheckout();

        assertEquals(new Integer(0), toTest.countScannedItems());
    }

    @Test
    void canRegisterCurrentGroceriesAndTheirPrices() {
        GroceryItem groceryA = new GroceryItem("A", new BigDecimal("5.0"));
        GroceryItem groceryB = new GroceryItem("B", new BigDecimal("6.55"));
        GroceryItem groceryC = new GroceryItem("C", new BigDecimal("4.22"));
        HashMap<String, GroceryItem> itemsInStore = new HashMap<>();
        itemsInStore.put(groceryA.getStockKeepingUnit(), groceryA);
        itemsInStore.put(groceryB.getStockKeepingUnit(), groceryB);
        itemsInStore.put(groceryC.getStockKeepingUnit(), groceryC);

        // First we will need to somehow register the items and their price
        SuperMarketWarehouse warehouse = new SuperMarketWarehouse();
        warehouse.storeItem(groceryA);
        warehouse.storeItem(groceryB);
        warehouse.storeItem(groceryC);


        assertTrue(itemsInStore.equals(warehouse.getItemsInStock()));
    }

    @Test
    void canReturnTheTotalCheckoutPriceForAllItemsScanned() {
        SuperMarketWarehouse warehouse = new SuperMarketWarehouse();
        warehouse.storeItem(new GroceryItem("A", new BigDecimal("5.0")));
        warehouse.storeItem(new GroceryItem("B", new BigDecimal("6.55")));
        warehouse.storeItem(new GroceryItem("C", new BigDecimal("4.22")));
        warehouse.storeItem(new GroceryItem("A", new BigDecimal("5.0")));

        SuperMarketCheckout toTest = new SuperMarketCheckout(warehouse);
        toTest.scanItem("A");
        toTest.scanItem("B");
        toTest.scanItem("C");

        assertEquals(new BigDecimal("15.77"), toTest.calculateTotal());
    }

    @Test
    void shouldReturnTotalOfZeroIfWeaddItemThatDoesNotExist() {

    }

    @Test
    void shouldReturnZeroTotalIfThereAreNoItemsScanned() {

    }

    @Test
    void someTestAboutNegativePricesValidation() {
        // :)
    }


}