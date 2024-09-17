package com.gabrielluciano.employeeservice.domain.dto;

import java.math.BigDecimal;

public record UpdateEmployeeRequest(String name, BigDecimal baseSalary, Long positionId) {
}
