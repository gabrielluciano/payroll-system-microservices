package com.gabrielluciano.payrollservice.domain.service;

import java.util.List;

import com.gabrielluciano.payrollservice.domain.dto.InssTaxRate;

public interface InssTaxService {

    List<InssTaxRate> getTaxRates();
}

