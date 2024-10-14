package com.gabrielluciano.payrollservice.domain.dto;

import java.math.BigDecimal;

public record InssTaxRate(
    Long id,
    BigDecimal minimumSalaryThreshold,
    BigDecimal maximumSalaryThreshold,
    BigDecimal rate
) implements Comparable<InssTaxRate> {

    @Override
    public int compareTo(InssTaxRate o) {
        return minimumSalaryThreshold.compareTo(o.minimumSalaryThreshold);
    }
}
