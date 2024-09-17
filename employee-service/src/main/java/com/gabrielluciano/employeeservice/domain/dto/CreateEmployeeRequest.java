package com.gabrielluciano.employeeservice.domain.dto;

import com.gabrielluciano.employeeservice.domain.model.Employee;

import java.math.BigDecimal;

public record CreateEmployeeRequest(String name, String cpf, Double baseSalary, Long positionId) {

    public Employee toModel() {
        return new Employee(cpf, name, BigDecimal.valueOf(baseSalary), null);
    }
}
