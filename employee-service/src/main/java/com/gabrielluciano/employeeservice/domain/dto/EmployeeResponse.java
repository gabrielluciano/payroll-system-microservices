package com.gabrielluciano.employeeservice.domain.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.gabrielluciano.employeeservice.domain.model.Employee;

public record EmployeeResponse(String name, String cpf, Long baseSalary,
                               @JsonAlias("position") PositionResponse positionResponse) {

    public static EmployeeResponse fromModel(Employee employee) {
        return new EmployeeResponse(employee.getName(), employee.getCpf(), employee.getBaseSalary().longValue(),
                PositionResponse.fromModel(employee.getPosition()));
    }
}
