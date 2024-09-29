package com.gabrielluciano.employeeservice.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record UpdateEmployeeRequest(
        @NotBlank String name,
        @NotNull @Positive BigDecimal baseSalary,
        Long positionId
) {
}
