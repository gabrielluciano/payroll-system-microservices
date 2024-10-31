package com.gabrielluciano.payrollservice.domain.dto;

import java.math.BigDecimal;

public record EmployeeResponse(String name, String cpf, BigDecimal baseSalary, EmployeePosition position) {
}
