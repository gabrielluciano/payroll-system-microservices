package com.gabrielluciano.employeeservice.domain.service;

import com.gabrielluciano.employeeservice.domain.dto.CreatePositionRequest;
import com.gabrielluciano.employeeservice.domain.dto.PositionResponse;

import java.util.List;

public interface PositionService {

    List<PositionResponse> list();

    PositionResponse save(CreatePositionRequest createPositionRequest);
}
