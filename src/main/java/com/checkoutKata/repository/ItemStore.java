package com.checkoutKata.repository;

import com.checkoutKata.exceptions.ItemNotFoundException;
import com.checkoutKata.model.Item;

import java.math.BigDecimal;
import java.util.HashMap;

public interface ItemStore {
    void storeItem(Item item);
    void removeItem(Item item);
    HashMap<String, Item> getItemsInStock();
    BigDecimal retrieveItemPrice(String stockKeepingUnit) throws ItemNotFoundException;
}
