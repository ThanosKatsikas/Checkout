package com.checkoutKata.services;

import com.checkoutKata.exceptions.ItemNotFoundException;
import com.checkoutKata.model.MultiBuyOffer;
import com.checkoutKata.model.Offer;
import com.checkoutKata.repository.SuperMarketWarehouse;

import java.math.BigDecimal;
import java.util.HashMap;

public class SuperMarketCheckout implements Checkout {
    private HashMap<String, Integer> scannedItems;
    private final SuperMarketWarehouse warehouse;
    private final SuperMarketOffersService offersService;

    public SuperMarketCheckout(SuperMarketWarehouse warehouse, SuperMarketOffersService offersService) {
        this.scannedItems = new HashMap<String, Integer>();
        this.warehouse = warehouse;
        this.offersService = offersService;
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
        return scannedItems.entrySet().stream()
                .map( entry -> {
                    Offer entryOffer = offersService.getOffer(entry.getKey());
                    if(entryOffer != null) {
                        // The quotient of Items Scanned and Number of Items for the offer,
                        // multiplied by the discount of the offer.
                        return entryOffer.getDiscount()
                                .multiply(
                                        BigDecimal.valueOf(entry.getValue() / ((MultiBuyOffer)entryOffer).getNumberOfItems()));
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    public BigDecimal calculateAmountToPay() {
        return calculateTotal().subtract(calculateSavings());
    }
}
