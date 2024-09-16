package com.gabrielluciano.employeeservice.domain.service.impl;

import com.gabrielluciano.employeeservice.domain.dto.CreatePositionRequest;
import com.gabrielluciano.employeeservice.domain.model.Position;
import com.gabrielluciano.employeeservice.domain.service.PositionService;
import com.gabrielluciano.employeeservice.infra.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {

    private final PositionRepository repository;

    @Override
    @Transactional
    public Position save(CreatePositionRequest createPositionRequest) {
        return repository.save(createPositionRequest.toModel());
    }
}
