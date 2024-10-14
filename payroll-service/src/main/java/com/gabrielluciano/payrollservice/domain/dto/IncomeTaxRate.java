package com.gabrielluciano.payrollservice.domain.dto;

import java.math.BigDecimal;

public record IncomeTaxRate(
    Long id,
    BigDecimal minimumSalaryThreshold,
    BigDecimal maximumSalaryThreshold,
    BigDecimal rate,
    BigDecimal deduction
) implements Comparable<IncomeTaxRate> {

    @Override
    public int compareTo(IncomeTaxRate o) {
        return minimumSalaryThreshold.compareTo(o.minimumSalaryThreshold);
    }
}
