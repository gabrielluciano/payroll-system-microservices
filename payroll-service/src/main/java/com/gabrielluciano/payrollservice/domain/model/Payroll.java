package com.gabrielluciano.payrollservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "payrolls", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employeeCpf", "year", "month"})
})
@SequenceGenerator(name = Payroll.SEQUENCE_NAME, sequenceName = Payroll.SEQUENCE_NAME)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {

    public static final String SEQUENCE_NAME = "sequence_payrolls";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @Column(length = 14, nullable = false)
    private String employeeCpf;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal grossPay;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal netPay;
}
