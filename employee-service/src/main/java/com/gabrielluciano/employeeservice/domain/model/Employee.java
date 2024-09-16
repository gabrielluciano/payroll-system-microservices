package com.gabrielluciano.employeeservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String cpf;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal baseSalary;

    @ManyToOne
    private Position position;
}
