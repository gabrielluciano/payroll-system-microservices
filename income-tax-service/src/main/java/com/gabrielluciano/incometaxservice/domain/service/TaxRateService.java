package com.gabrielluciano.incometaxservice.domain.service;

import com.gabrielluciano.incometaxservice.domain.dto.CreateTaxRateRequest;
import com.gabrielluciano.incometaxservice.domain.dto.TaxRateResponse;

import java.util.List;

public interface TaxRateService {

    Long save(CreateTaxRateRequest createTaxRateRequest);

    List<TaxRateResponse> list();

    void deleteById(Long id);
}
