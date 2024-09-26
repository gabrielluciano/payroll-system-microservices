package com.gabrielluciano.incometaxservice.domain.dto;

import com.gabrielluciano.incometaxservice.domain.model.TaxRate;

import java.math.BigDecimal;

public record TaxRateResponse(Long id,
                              BigDecimal minimumSalaryThreshold,
                              BigDecimal maximumSalaryThreshold,
                              BigDecimal rate,
                              BigDecimal deduction
) {

    public static TaxRateResponse fromModel(TaxRate taxRate) {
        return new TaxRateResponse(taxRate.getId(), taxRate.getMinimumSalaryThreshold(), taxRate.getMaximumSalaryThreshold(),
                taxRate.getRate(), taxRate.getDeduction());
    }
}
