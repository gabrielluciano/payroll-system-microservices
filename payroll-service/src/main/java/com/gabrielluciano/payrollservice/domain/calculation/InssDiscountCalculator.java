package com.gabrielluciano.payrollservice.domain.calculation;

import java.math.BigDecimal;

public interface InssDiscountCalculator {

    BigDecimal calculateDiscount(BigDecimal grossPay);
}
