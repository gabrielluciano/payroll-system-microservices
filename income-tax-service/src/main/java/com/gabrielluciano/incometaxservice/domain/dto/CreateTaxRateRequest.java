package com.gabrielluciano.incometaxservice.domain.dto;

import com.gabrielluciano.incometaxservice.domain.model.TaxRate;
import com.gabrielluciano.incometaxservice.domain.validation.ValidThresholds;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@ValidThresholds
public record CreateTaxRateRequest(
        @NotNull @DecimalMin("0.0") BigDecimal minimumSalaryThreshold,
        @NotNull @DecimalMin("0.0") BigDecimal maximumSalaryThreshold,
        @NotNull @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal rate,
        @NotNull @DecimalMin("0.0") BigDecimal deduction
) {

    public TaxRate toModel() {
        return new TaxRate(minimumSalaryThreshold, maximumSalaryThreshold, rate, deduction);
    }
}
