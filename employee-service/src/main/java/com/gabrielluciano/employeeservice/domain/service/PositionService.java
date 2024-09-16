package com.gabrielluciano.employeeservice.domain.service;

import com.gabrielluciano.employeeservice.domain.dto.CreatePositionRequest;
import com.gabrielluciano.employeeservice.domain.model.Position;

public interface PositionService {

    Position save(CreatePositionRequest createPositionRequest);
}
