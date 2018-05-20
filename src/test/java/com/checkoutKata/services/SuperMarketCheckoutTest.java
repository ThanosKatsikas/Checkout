package com.checkoutKata.services;


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
    private SuperMarketOfferStore offerStore;
    private SuperMarketOffersService offersService;

    @BeforeEach
    void prepareSuperMarket () {
        warehouse = new SuperMarketWarehouse();
        offerStore = new SuperMarketOfferStore();
        offersService = new SuperMarketOffersService(offerStore);
        checkout = new SuperMarketCheckout(warehouse, offersService);
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

        assertEquals( checkout.calculateTotal(), new BigDecimal("15.77"));
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
        assertTrue(!offersService.hasOfferFor("A"));
    }

    @Test
    void serviceShouldReturnTrueIfWeHaveAnOffer(){
        GroceryItem groceryItem = new GroceryItem("A", new BigDecimal("5.0"));
        MultiBuyOffer offerA = new MultiBuyOffer(groceryItem, 2, new BigDecimal("8.0"));

        offersService.registerOffer(offerA);

        assertTrue(offersService.hasOfferFor("A"));
    }

    @Test
    void checkoutShouldCalculateSavingsIfThereIsAnOfferThatAppliesToTheItemsScanned() {
        GroceryItem groceryItemA = new GroceryItem("A", new BigDecimal("5.00"));
        GroceryItem groceryItemB = new GroceryItem("B", new BigDecimal("3.00"));
        GroceryItem groceryItemC = new GroceryItem("C", new BigDecimal("2.00"));
        warehouse.storeItem(groceryItemA);
        warehouse.storeItem(groceryItemB);
        warehouse.storeItem(groceryItemC);

        MultiBuyOffer offerA = new MultiBuyOffer(groceryItemA, 2, new BigDecimal("8.00"));
        offersService.registerOffer(offerA);

        checkout.scanItem("A");
        checkout.scanItem("B");
        checkout.scanItem("C");
        checkout.scanItem("A");
        checkout.scanItem("B");

        assertEquals( new BigDecimal("18.00"),checkout.calculateTotal());
        assertEquals( new BigDecimal("2.00"), checkout.calculateSavings());
    }

    @Test
    void ShouldCalculateSavingsIfThereAreMultipleOffersThatApplyToTheItemsScanned() {
        GroceryItem groceryItemA = new GroceryItem("A", new BigDecimal("3.50"));
        GroceryItem groceryItemB = new GroceryItem("B", new BigDecimal("2.50"));
        GroceryItem groceryItemC = new GroceryItem("C", new BigDecimal("1.00"));
        warehouse.storeItem(groceryItemA);
        warehouse.storeItem(groceryItemB);
        warehouse.storeItem(groceryItemC);

        MultiBuyOffer offerA = new MultiBuyOffer(groceryItemA, 2, new BigDecimal("5.00"));
        MultiBuyOffer offerB = new MultiBuyOffer(groceryItemB, 3, new BigDecimal("6.00"));
        offersService.registerOffer(offerA);
        offersService.registerOffer(offerB);

        checkout.scanItem("A");
        checkout.scanItem("B");
        checkout.scanItem("A");
        checkout.scanItem("C");
        checkout.scanItem("B");
        checkout.scanItem("A");
        checkout.scanItem("C");
        checkout.scanItem("A");
        checkout.scanItem("B");
        checkout.scanItem("A");

        assertEquals( new BigDecimal("27.00"),checkout.calculateTotal());
        assertEquals( new BigDecimal("5.50"), checkout.calculateSavings());
    }

    @Test
    void shouldPassTheTestWithTheDataProvidedInTheExersice() {
        GroceryItem groceryItemA = new GroceryItem("A", new BigDecimal("50"));
        GroceryItem groceryItemB = new GroceryItem("B", new BigDecimal("30"));
        warehouse.storeItem(groceryItemA);
        warehouse.storeItem(groceryItemB);

        MultiBuyOffer offerA = new MultiBuyOffer(groceryItemA, 3, new BigDecimal("130"));
        MultiBuyOffer offerB = new MultiBuyOffer(groceryItemB, 2, new BigDecimal("45"));
        offersService.registerOffer(offerA);
        offersService.registerOffer(offerB);

        checkout.scanItem("B");
        checkout.scanItem("A");
        checkout.scanItem("B");


        assertEquals( new BigDecimal("110"),checkout.calculateTotal());
        assertEquals( new BigDecimal("15"),checkout.calculateSavings());
    }

    @Test
    void shouldReturnTheAmountToPay() {
        GroceryItem groceryItemA = new GroceryItem("A", new BigDecimal("50"));
        GroceryItem groceryItemB = new GroceryItem("B", new BigDecimal("30"));
        warehouse.storeItem(groceryItemA);
        warehouse.storeItem(groceryItemB);

        MultiBuyOffer offerA = new MultiBuyOffer(groceryItemA, 3, new BigDecimal("130"));
        MultiBuyOffer offerB = new MultiBuyOffer(groceryItemB, 2, new BigDecimal("45"));
        offersService.registerOffer(offerA);
        offersService.registerOffer(offerB);

        checkout.scanItem("B");
        checkout.scanItem("A");
        checkout.scanItem("B");


        assertEquals( new BigDecimal("95"), checkout.calculateAmountToPay());

    }

    @Test
    void ammuntToPayshouldReturnZeroIfWeHaveScannedNoScannedItems() {
        GroceryItem groceryItemA = new GroceryItem("A", new BigDecimal("50"));
        GroceryItem groceryItemB = new GroceryItem("B", new BigDecimal("30"));
        warehouse.storeItem(groceryItemA);
        warehouse.storeItem(groceryItemB);

        MultiBuyOffer offerA = new MultiBuyOffer(groceryItemA, 3, new BigDecimal("130"));
        MultiBuyOffer offerB = new MultiBuyOffer(groceryItemB, 2, new BigDecimal("45"));
        offersService.registerOffer(offerA);
        offersService.registerOffer(offerB);

        assertEquals(BigDecimal.ZERO, checkout.calculateAmountToPay());
    }

    @Test
    void someTestAboutNegativePricesValidation() {
        // We should add tests for validating input in the case we implemented a more dynamic way of loading items and offers.
    }


}