package com.gabrielluciano.insstaxservice.domain.service;

import com.gabrielluciano.insstaxservice.domain.dto.CreateTaxRateRequest;

public interface TaxRateService {

    Long save(CreateTaxRateRequest createTaxRateRequest);
}
