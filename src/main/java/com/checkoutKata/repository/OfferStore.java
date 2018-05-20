package com.checkoutKata.repository;

import com.checkoutKata.exceptions.OfferNotFoundException;
import com.checkoutKata.model.Offer;

import java.util.HashSet;

public interface OfferStore {
    void storeOffer(Offer offer);
    Offer getOffer(String stockKeepingUnit) throws OfferNotFoundException;
    HashSet<Offer> getOffers(String stockKeepingUnit);
}
