package com.gabrielluciano.employeeservice.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;

@Entity
@Table(name = "employees")
@SequenceGenerator(name = Employee.SEQUENCE_NAME, sequenceName = Employee.SEQUENCE_NAME)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee {

    public static final String SEQUENCE_NAME = "sequence_employee";

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
