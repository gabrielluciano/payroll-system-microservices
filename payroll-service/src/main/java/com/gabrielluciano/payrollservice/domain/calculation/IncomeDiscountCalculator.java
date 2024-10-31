package com.gabrielluciano.payrollservice.domain.calculation;

import java.math.BigDecimal;

public interface IncomeDiscountCalculator {

    BigDecimal calculateDiscount(BigDecimal grossPay);
}
