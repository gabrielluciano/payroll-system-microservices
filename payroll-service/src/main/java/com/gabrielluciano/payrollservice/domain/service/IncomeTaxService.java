package com.gabrielluciano.payrollservice.domain.service;

import java.util.List;

import com.gabrielluciano.payrollservice.domain.dto.IncomeTaxRate;

public interface IncomeTaxService {

    List<IncomeTaxRate> getTaxRates();
}
