package com.gabrielluciano.incometaxservice.domain.service;

import com.gabrielluciano.incometaxservice.domain.dto.CreateTaxRateRequest;

public interface TaxRateService {

    Long save(CreateTaxRateRequest createTaxRateRequest);
}
