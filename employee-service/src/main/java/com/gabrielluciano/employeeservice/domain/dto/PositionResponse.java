package com.gabrielluciano.employeeservice.domain.dto;

import com.gabrielluciano.employeeservice.domain.model.Position;

public record PositionResponse(Long id, String name) {

    public static PositionResponse fromModel(Position position) {
        return new PositionResponse(position.getId(), position.getName());
    }
}
