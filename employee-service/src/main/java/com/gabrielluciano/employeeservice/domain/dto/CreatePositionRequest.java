package com.gabrielluciano.employeeservice.domain.dto;

import com.gabrielluciano.employeeservice.domain.model.Position;

public record CreatePositionRequest(String name) {

    public Position toModel() {
        return new Position(name);
    }
}
