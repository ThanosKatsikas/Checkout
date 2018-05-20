package com.checkoutKata.model;

import java.math.BigDecimal;

public class MultiBuyOffer extends Offer {
    private final int numberOfItems;
    private final BigDecimal specialPrice;

    public MultiBuyOffer(Item item, int numberOfItems, BigDecimal specialPrice) {
        super(item);
        this.numberOfItems = numberOfItems;
        this.specialPrice = specialPrice;

        calculateReduction();
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public BigDecimal getSpecialPrice() {
        return specialPrice;
    }

    /**
     * In this type of offer, the total discount if we have numberOfItems checked out
     * would be the number of items multiplied by their original price minus the
     * special price.
     */
    @Override
    void calculateReduction() {
        this.discount = this.item.getUnitPrice()
                .multiply(new BigDecimal(this.numberOfItems))
                .subtract(this.specialPrice);
    }
}
