package com.checkoutKata.services;

import java.math.BigDecimal;
import java.util.HashMap;

public class SuperMarketCheckout implements Checkout {
    private HashMap<String, Integer> scannedItems;

    public SuperMarketCheckout() {
        this.scannedItems = new HashMap<String, Integer>();
    }
    
    public BigDecimal calculateTotal() {
        return null;
    }

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
                .mapToInt(Integer::intValue)
                .sum();
    }
}
