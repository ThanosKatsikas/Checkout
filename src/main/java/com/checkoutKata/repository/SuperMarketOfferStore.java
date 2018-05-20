package com.checkoutKata.repository;

import com.checkoutKata.exceptions.OfferNotFoundException;
import com.checkoutKata.model.Offer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class SuperMarketOfferStore implements OfferStore {
    HashMap<String, Offer> offersStore;

    public SuperMarketOfferStore() {
        this.offersStore = new HashMap<>();
    }

    @Override
    public void storeOffer(Offer offer) {
        offersStore.put(offer.getItem().getStockKeepingUnit(), offer);
    }

    @Override
    public Offer getOffer(String stockKeepingUnit) throws OfferNotFoundException {
        Optional<Offer> retrievedOffer = Optional.ofNullable(offersStore.get(stockKeepingUnit));

        return retrievedOffer.orElseThrow(OfferNotFoundException::new);
    }

    /**
     * In reality we could have more than one offers associated with the same item.
     * Hence why we may want to be able to get all of them and apply some sort of rule
     * later.
     * This is beyond the scope of the exercise so I'll leave it as is.
     */
    @Override
    public HashSet<Offer> getOffers(String stockKeepingUnit) {
       return null;
    }
}
