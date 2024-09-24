package com.gabrielluciano.insstaxservice.domain.dto;

import com.gabrielluciano.insstaxservice.domain.model.TaxRate;

import java.math.BigDecimal;

public record CreateTaxRateRequest(BigDecimal minimumSalaryThreshold, BigDecimal maximumSalaryThreshold,
                                   BigDecimal taxRate) {

    public TaxRate toModel() {
        return new TaxRate(minimumSalaryThreshold, maximumSalaryThreshold, taxRate);
    }
}
