package com.checkoutKata.model;

import java.math.BigDecimal;

public abstract class Item {
    private String storeKeepingUnit;
    private BigDecimal unitPrice;

    public Item(String storeKeepingUnit, BigDecimal unitPrice) {
        this.storeKeepingUnit = storeKeepingUnit;
        this.unitPrice = unitPrice;
    }

    public String getStoreKeepingUnit() {
        return storeKeepingUnit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
