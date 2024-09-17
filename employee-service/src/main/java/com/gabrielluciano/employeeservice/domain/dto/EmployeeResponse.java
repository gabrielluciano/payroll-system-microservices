package com.gabrielluciano.employeeservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.gabrielluciano.employeeservice.domain.model.Employee;

import java.math.BigDecimal;

public record EmployeeResponse(String name, String cpf, BigDecimal baseSalary,
                               @JsonAlias("position") PositionResponse positionResponse) {

    public static EmployeeResponse fromModel(Employee employee) {
        return new EmployeeResponse(employee.getName(), employee.getCpf(), employee.getBaseSalary(),
                PositionResponse.fromModel(employee.getPosition()));
    }
}
