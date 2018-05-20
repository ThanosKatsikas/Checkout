package com.checkoutKata.model;

import java.math.BigDecimal;

public abstract class Offer {

    protected BigDecimal discount;
    protected final Item item;

    Offer(Item item) {
        this.item = item;
    }

    abstract void calculateReduction();

    public BigDecimal getDiscount() {
        return discount;
    }

    public Item getItem() {
        return item;
    }
}
