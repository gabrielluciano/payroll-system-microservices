package com.gabrielluciano.payrollservice.domain.service;

import java.math.BigDecimal;

public interface IncomeService {

    BigDecimal calculateDiscount(BigDecimal grossPay);
}
