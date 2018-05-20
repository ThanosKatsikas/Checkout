package com.checkoutKata.services;

import com.checkoutKata.model.Offer;

public interface OffersService {
    Boolean hasOfferFor(String stockKeepingUnit);
    void registerOffer(Offer offer);
}
