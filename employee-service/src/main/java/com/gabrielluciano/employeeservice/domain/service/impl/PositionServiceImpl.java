package com.gabrielluciano.employeeservice.domain.service.impl;

import com.gabrielluciano.employeeservice.domain.dto.CreatePositionRequest;
import com.gabrielluciano.employeeservice.domain.dto.PositionResponse;
import com.gabrielluciano.employeeservice.domain.service.PositionService;
import com.gabrielluciano.employeeservice.infra.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository repository;

    @Override
    public List<PositionResponse> list() {
        return repository.findAll().stream()
                .map(PositionResponse::fromModel)
                .toList();
    }

    @Override
    @Transactional
    public PositionResponse save(CreatePositionRequest createPositionRequest) {
        return PositionResponse.fromModel(repository.save(createPositionRequest.toModel()));
    }
}
