package com.checkoutKata.services;

import com.checkoutKata.exceptions.OfferNotFoundException;
import com.checkoutKata.model.Offer;
import com.checkoutKata.repository.SuperMarketOfferStore;

public class SuperMarketOffersService implements OffersService{
    private final SuperMarketOfferStore offerStore;

    public SuperMarketOffersService(SuperMarketOfferStore offerStore) {
        this.offerStore = offerStore;
    }

    @Override
    public Boolean hasOfferFor(String stockKeepingUnit) {
        try {
            if (offerStore.getOffer(stockKeepingUnit) != null) {
                return true;
            }
            return false;
        } catch (OfferNotFoundException e) {
            return false;
        }
    }

    public Offer getOffer(String stockKeepingUnit) {
        try {
            return offerStore.getOffer(stockKeepingUnit);
        } catch (OfferNotFoundException e) {
            return null;
        }
    }

    @Override
    public void registerOffer(Offer offer) {
        offerStore.storeOffer(offer);
    }
}
