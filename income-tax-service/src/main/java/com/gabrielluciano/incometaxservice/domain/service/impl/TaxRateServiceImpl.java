package com.gabrielluciano.incometaxservice.domain.service.impl;

import com.gabrielluciano.incometaxservice.domain.dto.CreateTaxRateRequest;
import com.gabrielluciano.incometaxservice.domain.service.TaxRateService;
import com.gabrielluciano.incometaxservice.infra.repository.TaxRateRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaxRateServiceImpl implements TaxRateService {

    private final TaxRateRepository repository;

    @Override
    @Transactional
    public Long save(@Valid CreateTaxRateRequest createTaxRateRequest) {
        return repository.save(createTaxRateRequest.toModel()).getId();
    }
}
