package com.checkoutKata.repository;

import com.checkoutKata.exceptions.ItemNotFoundException;
import com.checkoutKata.model.Item;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Optional;

public class SuperMarketWarehouse implements ItemStore {
    HashMap<String, Item> groceriesStock;

    public SuperMarketWarehouse() {
        groceriesStock = new HashMap<>();
    }

    @Override
    public void storeItem(Item item) {
        groceriesStock.put(item.getStockKeepingUnit(), item);
    }

    @Override
    public void removeItem(Item item) {
        groceriesStock.remove(item.getStockKeepingUnit());
    }

    @Override
    public HashMap<String, Item> getItemsInStock() {
        return groceriesStock;
    }

    @Override
    public BigDecimal retrieveItemPrice(String stockKeepingUnit) throws ItemNotFoundException {
        Optional<Item> returnedPrice = Optional.of(groceriesStock.get(stockKeepingUnit));

        return returnedPrice.orElseThrow(ItemNotFoundException::new).getUnitPrice();
    }
}
