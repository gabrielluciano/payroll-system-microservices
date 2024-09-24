package com.gabrielluciano.insstaxservice.domain.service.impl;

import com.gabrielluciano.insstaxservice.domain.dto.CreateTaxRateRequest;
import com.gabrielluciano.insstaxservice.domain.dto.TaxRateResponse;
import com.gabrielluciano.insstaxservice.domain.exception.InvalidTaxRateException;
import com.gabrielluciano.insstaxservice.domain.model.TaxRate;
import com.gabrielluciano.insstaxservice.domain.service.TaxRateService;
import com.gabrielluciano.insstaxservice.infra.repository.TaxRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaxRateServiceImpl implements TaxRateService {

    private final TaxRateRepository repository;

    @Override
    @Transactional
    public Long save(CreateTaxRateRequest createTaxRateRequest) {
        TaxRate taxRate = createTaxRateRequest.toModel();
        if (!taxRate.hasValidThresholds())
            throw new InvalidTaxRateException("Invalid thresholds");

        return repository.save(taxRate).getId();
    }

    @Override
    public List<TaxRateResponse> list() {
        return repository.findAll().stream()
                .sorted()
                .map(TaxRateResponse::fromModel)
                .toList();
    }
}
