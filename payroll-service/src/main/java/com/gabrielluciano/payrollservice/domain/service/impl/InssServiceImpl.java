package com.gabrielluciano.payrollservice.domain.service.impl;

import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gabrielluciano.payrollservice.domain.dto.InssTaxRate;
import com.gabrielluciano.payrollservice.domain.service.InssService;
import com.gabrielluciano.payrollservice.domain.service.InssTaxService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InssServiceImpl implements InssService {

    private final InssTaxService taxService;

    @Override
    public BigDecimal calculateDiscount(BigDecimal grossPay) {
        if (grossPay == null)
            return valueOf(0.00).setScale(2, RoundingMode.HALF_UP);

        final var taxRates = taxService.getTaxRates().stream().sorted().toList();

        return calculateTax(grossPay, taxRates).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTax(BigDecimal grossPay, List<InssTaxRate> taxRates) {
        var tax = valueOf(0.00);
        for (InssTaxRate taxRate : taxRates) {
            boolean isSalaryBetweenRange = testSalaryBetweenRange(grossPay, taxRate);
            if (isSalaryBetweenRange)
                return tax.add(grossPay.subtract(taxRate.minimumSalaryThreshold()).multiply(taxRate.rate()));
            tax = tax.add(taxRate.maximumSalaryThreshold().subtract(taxRate.minimumSalaryThreshold()).multiply(taxRate.rate()));
        }
        return tax;
    }

    private boolean testSalaryBetweenRange(BigDecimal grossPay, InssTaxRate taxRate) {
        return grossPay.compareTo(taxRate.minimumSalaryThreshold()) >= 0
            && grossPay.compareTo(taxRate.maximumSalaryThreshold()) <= 0;
    }
}

