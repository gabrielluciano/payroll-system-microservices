package com.gabrielluciano.employeeservice.domain.dto;

import com.gabrielluciano.employeeservice.domain.model.Employee;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

public record CreateEmployeeRequest(
        @NotBlank String name,
        @NotBlank @CPF String cpf,
        @NotNull @Positive BigDecimal baseSalary,
        Long positionId
) {

    public Employee toModel() {
        return new Employee(cpf, name, baseSalary, null);
    }
}
