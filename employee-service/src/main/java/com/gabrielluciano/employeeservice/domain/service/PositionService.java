package com.gabrielluciano.employeeservice.domain.service;

import com.gabrielluciano.employeeservice.domain.dto.CreatePositionRequest;
import com.gabrielluciano.employeeservice.domain.dto.PositionResponse;
import com.gabrielluciano.employeeservice.domain.model.Position;

import java.util.List;

public interface PositionService {

    List<PositionResponse> list();
    Position save(CreatePositionRequest createPositionRequest);
}
