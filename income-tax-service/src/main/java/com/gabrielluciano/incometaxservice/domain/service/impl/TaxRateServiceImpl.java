package com.gabrielluciano.incometaxservice.domain.service.impl;

import com.gabrielluciano.incometaxservice.domain.dto.CreateTaxRateRequest;
import com.gabrielluciano.incometaxservice.domain.dto.TaxRateResponse;
import com.gabrielluciano.incometaxservice.domain.exception.EntityNotFoundException;
import com.gabrielluciano.incometaxservice.domain.model.TaxRate;
import com.gabrielluciano.incometaxservice.domain.service.TaxRateService;
import com.gabrielluciano.incometaxservice.infra.repository.TaxRateRepository;
import jakarta.validation.Valid;
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
    public Long save(@Valid CreateTaxRateRequest createTaxRateRequest) {
        return repository.save(createTaxRateRequest.toModel()).getId();
    }

    @Override
    public List<TaxRateResponse> list() {
        return repository.findAll().stream()
                .sorted()
                .map(TaxRateResponse::fromModel)
                .toList();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.findById(id).orElseThrow(() -> new EntityNotFoundException(id, TaxRate.class));
        repository.deleteById(id);
    }
}
