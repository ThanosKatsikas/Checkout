package com.checkoutKata.services;

import com.checkoutKata.exceptions.ItemNotFoundException;
import com.checkoutKata.repository.SuperMarketWarehouse;

import java.math.BigDecimal;
import java.util.HashMap;

public class SuperMarketCheckout implements Checkout {
    private HashMap<String, Integer> scannedItems;
    private final SuperMarketWarehouse warehouse;

    //TODO: This shouldn't be called, adjust first test.
    public SuperMarketCheckout() {
        this.scannedItems = new HashMap<String, Integer>();
        this.warehouse = null;
    }

    public SuperMarketCheckout(SuperMarketWarehouse warehouse) {
        this.scannedItems = new HashMap<String, Integer>();
        this.warehouse = warehouse;
    }

    @Override
    public BigDecimal calculateTotal() {
        return scannedItems.entrySet().stream()
                .map( entry -> {
                    try {
                        // Multiply number of the same item we've scanned with their price
                        return warehouse.retrieveItemPrice(entry.getKey()).multiply(new BigDecimal(entry.getValue()));
                    } catch (ItemNotFoundException e) {
                        return BigDecimal.ZERO;
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

    }

    @Override
    public BigDecimal calculateSavings() {
        return null;
    }

    public void scanItem(String stockKeepingUnit) {
        if(!scannedItems.containsKey(stockKeepingUnit)) {
            scannedItems.put(stockKeepingUnit, 1);
        } else {
            scannedItems.put(stockKeepingUnit, scannedItems.get(stockKeepingUnit) + 1);
        }
    }

    public Integer countScannedItems() {
        return this.scannedItems
                .values()
                .stream()
                .reduce(0, (accumulator, next) -> accumulator = accumulator + next);
    }
}
