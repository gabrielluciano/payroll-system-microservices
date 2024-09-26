package com.gabrielluciano.insstaxservice.domain.validation;

import com.gabrielluciano.insstaxservice.domain.dto.CreateTaxRateRequest;
import com.gabrielluciano.insstaxservice.domain.model.TaxRate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

public class ValidThresholdsValidator implements ConstraintValidator<ValidThresholds, Object> {

    @Override
    public void initialize(ValidThresholds constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj instanceof TaxRate taxRate)
            return validateThresholds(taxRate);

        if (obj instanceof CreateTaxRateRequest createTaxRateRequest)
            return validateThresholds(createTaxRateRequest.toModel());

        return true;
    }

    private boolean validateThresholds(TaxRate taxRate) {
        return taxRate.getMinimumSalaryThreshold() != null
                && taxRate.getMaximumSalaryThreshold() != null
                && taxRate.getMinimumSalaryThreshold().compareTo(BigDecimal.ZERO) >= 0
                && taxRate.getMaximumSalaryThreshold().compareTo(BigDecimal.ZERO) >= 0
                && taxRate.getMaximumSalaryThreshold().compareTo(taxRate.getMinimumSalaryThreshold()) > 0;
    }
}
