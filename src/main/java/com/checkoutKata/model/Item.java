package com.checkoutKata.model;

import java.math.BigDecimal;

public abstract class Item {
    private String stockKeepingUnit;
    private BigDecimal unitPrice;

    public Item(String stockKeepingUnit, BigDecimal unitPrice) {
        this.stockKeepingUnit = stockKeepingUnit;
        this.unitPrice = unitPrice;
    }

    public String getStockKeepingUnit() {
        return stockKeepingUnit;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }
}
