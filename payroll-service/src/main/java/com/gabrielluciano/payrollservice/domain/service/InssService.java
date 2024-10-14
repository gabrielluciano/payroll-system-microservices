package com.gabrielluciano.payrollservice.domain.service;

import java.math.BigDecimal;

public interface InssService {

    BigDecimal calculateDiscount(BigDecimal grossPay);
}
