package com.gabrielluciano.insstaxservice.domain.dto;

import com.gabrielluciano.insstaxservice.domain.model.TaxRate;
import com.gabrielluciano.insstaxservice.domain.validation.ValidThresholds;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@ValidThresholds
public record CreateTaxRateRequest(
        @NotNull @DecimalMin("0.0") BigDecimal minimumSalaryThreshold,
        @NotNull @DecimalMin("0.0") BigDecimal maximumSalaryThreshold,
        @NotNull @DecimalMin("0.0") @DecimalMax("1.0") BigDecimal rate
) {

    public TaxRate toModel() {
        return new TaxRate(minimumSalaryThreshold, maximumSalaryThreshold, rate);
    }
}
