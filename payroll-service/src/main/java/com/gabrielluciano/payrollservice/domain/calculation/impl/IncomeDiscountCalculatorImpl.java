package com.gabrielluciano.payrollservice.domain.calculation.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gabrielluciano.payrollservice.domain.calculation.IncomeDiscountCalculator;
import com.gabrielluciano.payrollservice.domain.model.IncomeTaxRate;
import com.gabrielluciano.payrollservice.domain.provider.IncomeTaxRateProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeDiscountCalculatorImpl implements IncomeDiscountCalculator {

    private final IncomeTaxRateProvider taxRateProvider;

    @Override
    public BigDecimal calculateDiscount(BigDecimal grossPay) {
        BigDecimal tax = BigDecimal.valueOf(0.00);
        if (grossPay == null) return tax.setScale(2, RoundingMode.HALF_UP);

        List<IncomeTaxRate> taxRates = taxRateProvider.getTaxRates().stream().sorted().toList();

        IncomeTaxRate taxRateForPay = getTaxRateForSalaryRange(grossPay, taxRates);
        if (taxRateForPay != null) {
            tax = tax.add(grossPay.multiply(taxRateForPay.rate()).subtract(taxRateForPay.deduction()));
        }

        return tax.setScale(2, RoundingMode.HALF_UP);
    }

    private IncomeTaxRate getTaxRateForSalaryRange(BigDecimal grossPay, List<IncomeTaxRate> taxRates) {
        for (IncomeTaxRate taxRate : taxRates) {
            boolean isInSalaryRange = checkSalaryRange(grossPay, taxRate);
            if (isInSalaryRange) return taxRate;
        }
        return null;
    }

    private boolean checkSalaryRange(BigDecimal grossPay, IncomeTaxRate taxRate) {
        return grossPay.compareTo(taxRate.minimumSalaryThreshold()) >= 0
            && grossPay.compareTo(taxRate.maximumSalaryThreshold()) <= 0;
    }
}

