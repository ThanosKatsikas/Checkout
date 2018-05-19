package com.checkoutKata.model;

import java.math.BigDecimal;

public abstract class Offer {
    private Integer numberOfItems;
    private BigDecimal specialPrice;

    public Offer(Integer numberOfItems, BigDecimal specialPrice) {
        this.numberOfItems = numberOfItems;
        this.specialPrice = specialPrice;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public BigDecimal getSpecialPrice() {
        return specialPrice;
    }
}
