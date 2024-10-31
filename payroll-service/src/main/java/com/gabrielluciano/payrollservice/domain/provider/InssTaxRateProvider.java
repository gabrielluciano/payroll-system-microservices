package com.gabrielluciano.payrollservice.domain.provider;

import java.util.List;

import com.gabrielluciano.payrollservice.domain.dto.InssTaxRate;

public interface InssTaxRateProvider {

    List<InssTaxRate> getTaxRates();
}

