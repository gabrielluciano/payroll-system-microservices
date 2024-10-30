package com.gabrielluciano.employeeservice.domain.model;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    @Id
    @NotBlank
    @CPF
    private String cpf;

    @Column(nullable = false)
    @NotBlank
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    @NotNull
    @Positive
    private BigDecimal baseSalary;

    @ManyToOne
    private Position position;
}
