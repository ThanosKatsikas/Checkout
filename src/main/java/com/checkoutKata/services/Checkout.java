package com.checkoutKata.services;

import java.math.BigDecimal;

public interface Checkout {
    BigDecimal calculateTotal();
    BigDecimal calculateSavings();
    BigDecimal calculateAmountToPay();
}
