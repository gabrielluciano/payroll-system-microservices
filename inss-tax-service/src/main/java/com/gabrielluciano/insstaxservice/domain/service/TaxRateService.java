package com.gabrielluciano.insstaxservice.domain.service;

import com.gabrielluciano.insstaxservice.domain.dto.CreateTaxRateRequest;
import com.gabrielluciano.insstaxservice.domain.dto.TaxRateResponse;

import java.util.List;

public interface TaxRateService {

    Long save(CreateTaxRateRequest createTaxRateRequest);

    List<TaxRateResponse> list();
}
