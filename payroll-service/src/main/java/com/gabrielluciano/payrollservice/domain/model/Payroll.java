package com.gabrielluciano.payrollservice.domain.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payrolls", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employeeCpf", "year", "month"})
})
@SequenceGenerator(name = Payroll.SEQUENCE_NAME, sequenceName = Payroll.SEQUENCE_NAME)
@Getter
@Setter
@Builder
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
