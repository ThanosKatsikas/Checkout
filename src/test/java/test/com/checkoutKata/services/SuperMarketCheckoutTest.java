package test.com.checkoutKata.services;


import com.checkoutKata.exceptions.OfferNotFoundException;
import com.checkoutKata.model.GroceryItem;
import com.checkoutKata.model.MultiBuyOffer;
import com.checkoutKata.repository.SuperMarketOfferStore;
import com.checkoutKata.repository.SuperMarketWarehouse;
import com.checkoutKata.services.SuperMarketCheckout;
import com.checkoutKata.services.SuperMarketOffersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SuperMarketCheckoutTest {

    private SuperMarketCheckout checkout;
    private SuperMarketWarehouse warehouse;

    @BeforeEach
    void prepareSuperMarket () {
        warehouse = new SuperMarketWarehouse();
        checkout = new SuperMarketCheckout(warehouse);
    }

    @Test
    void canScanItems() {
        checkout.scanItem(new GroceryItem("A", new BigDecimal("5.0")).getStockKeepingUnit());
        checkout.scanItem(new GroceryItem("B", new BigDecimal("6.0")).getStockKeepingUnit());
        checkout.scanItem(new GroceryItem("A", new BigDecimal("5.0")).getStockKeepingUnit());

        assertEquals(new Integer(3), checkout.countScannedItems());
    }

    @Test
    void shouldReturnZeroIfThereAreNoItemsScanned() {
        assertEquals(new Integer(0), checkout.countScannedItems());
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
        warehouse.storeItem(groceryA);
        warehouse.storeItem(groceryB);
        warehouse.storeItem(groceryC);

        assertTrue(itemsInStore.equals(warehouse.getItemsInStock()));
    }

    @Test
    void canReturnTheTotalCheckoutPriceForAllItemsScanned() {
        warehouse.storeItem(new GroceryItem("A", new BigDecimal("5.0")));
        warehouse.storeItem(new GroceryItem("B", new BigDecimal("6.55")));
        warehouse.storeItem(new GroceryItem("C", new BigDecimal("4.22")));
        warehouse.storeItem(new GroceryItem("A", new BigDecimal("5.0")));

        checkout.scanItem("A");
        checkout.scanItem("B");
        checkout.scanItem("C");

        assertEquals(new BigDecimal("15.77"), checkout.calculateTotal());
    }

    @Test
    void shouldReturnTotalOfZeroIfWeAddItemThatDoesNotExist() {
        checkout.scanItem("D");
        checkout.scanItem("E");

        assertEquals(BigDecimal.ZERO, checkout.calculateTotal());
    }

    @Test
    void shouldReturnZeroTotalIfThereAreNoItemsScanned() {
        assertEquals(BigDecimal.ZERO, checkout.calculateTotal());
    }

    @Test
    void canRegisterMultiBuyOffers() {
        GroceryItem groceryItem = new GroceryItem("A", new BigDecimal("5.0"));

        SuperMarketOfferStore offerStore = new SuperMarketOfferStore();

        MultiBuyOffer offerA = new MultiBuyOffer(groceryItem, 2, new BigDecimal("8.0"));

        offerStore.storeOffer(offerA);

        try {
            MultiBuyOffer toTest = (MultiBuyOffer) offerStore.getOffer("A");
            assertEquals(offerA.getNumberOfItems(), toTest.getNumberOfItems());
            assertEquals(offerA.getSpecialPrice(), toTest.getSpecialPrice());
            assertEquals(offerA.getItem().getStockKeepingUnit(),toTest.getItem().getStockKeepingUnit());
            assertEquals(offerA.getItem().getUnitPrice(), toTest.getItem().getUnitPrice());
        } catch (OfferNotFoundException e) {
            assertTrue(false);
        }
    }

    @Test
    void serviceShouldReturnFalseIfWeHaveNoOffer() {
        GroceryItem groceryItem = new GroceryItem("A", new BigDecimal("5.0"));
        MultiBuyOffer offerA = new MultiBuyOffer(groceryItem, 2, new BigDecimal("8.0"));

        SuperMarketOfferStore offerStore = new SuperMarketOfferStore();
        SuperMarketOffersService offersService = new SuperMarketOffersService(offerStore);

        assertTrue(!offersService.hasOfferFor("A"));
    }

    @Test
    void serviceShouldReturnTrueIfWeHaveAnOffer(){
        GroceryItem groceryItem = new GroceryItem("A", new BigDecimal("5.0"));
        MultiBuyOffer offerA = new MultiBuyOffer(groceryItem, 2, new BigDecimal("8.0"));

        SuperMarketOfferStore offerStore = new SuperMarketOfferStore();
        SuperMarketOffersService offersService = new SuperMarketOffersService(offerStore);

        offersService.registerOffer(offerA);

        assertTrue(offersService.hasOfferFor("A"));
    }


    @Test
    void someTestAboutNegativePricesValidation() {
        // :)
    }


}