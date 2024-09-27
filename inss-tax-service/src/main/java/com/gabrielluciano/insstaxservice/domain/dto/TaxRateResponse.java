package com.gabrielluciano.insstaxservice.domain.dto;

import com.gabrielluciano.insstaxservice.domain.model.TaxRate;

import java.math.BigDecimal;

public record TaxRateResponse(
        Long id,
        BigDecimal minimumSalaryThreshold,
        BigDecimal maximumSalaryThreshold,
        BigDecimal rate
) {
    public static TaxRateResponse fromModel(TaxRate taxRate) {

        return new TaxRateResponse(taxRate.getId(), taxRate.getMinimumSalaryThreshold(), taxRate.getMaximumSalaryThreshold(),
                taxRate.getRate());
    }
}
