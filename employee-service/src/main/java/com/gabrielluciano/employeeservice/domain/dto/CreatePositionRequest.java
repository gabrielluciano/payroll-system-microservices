package com.gabrielluciano.employeeservice.domain.dto;

import com.gabrielluciano.employeeservice.domain.model.Position;
import jakarta.validation.constraints.NotBlank;

public record CreatePositionRequest(@NotBlank String name) {

    public Position toModel() {
        return new Position(name);
    }
}
