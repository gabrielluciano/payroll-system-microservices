package com.gabrielluciano.payrollservice.domain.provider;

import java.util.List;

import com.gabrielluciano.payrollservice.domain.dto.IncomeTaxRate;

public interface IncomeTaxRateProvider {

    List<IncomeTaxRate> getTaxRates();
}
