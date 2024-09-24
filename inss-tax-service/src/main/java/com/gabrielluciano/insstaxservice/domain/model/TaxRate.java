package com.gabrielluciano.insstaxservice.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "tax_rate")
@SequenceGenerator(name = TaxRate.SEQUENCE_NAME, sequenceName = TaxRate.SEQUENCE_NAME)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaxRate implements Comparable<TaxRate> {

    public static final String SEQUENCE_NAME = "sequence_tax_rate";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    private Long id;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false, unique = true, precision = 10, scale = 2)
    private BigDecimal minimumSalaryThreshold;

    @NotNull
    @DecimalMin("0.0")
    @Column(nullable = false, unique = true, precision = 10, scale = 2)
    private BigDecimal maximumSalaryThreshold;

    @NotNull
    @Positive
    @DecimalMax("1.0")
    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal rate;

    public TaxRate(BigDecimal minimumSalaryThreshold, BigDecimal maximumSalaryThreshold, BigDecimal rate) {
        this.minimumSalaryThreshold = minimumSalaryThreshold;
        this.maximumSalaryThreshold = maximumSalaryThreshold;
        this.rate = rate;
    }

    @Override
    public int compareTo(TaxRate o) {
        return minimumSalaryThreshold.compareTo(o.minimumSalaryThreshold);
    }

    public boolean hasValidThresholds() {
        return (minimumSalaryThreshold != null
                && maximumSalaryThreshold != null
                && minimumSalaryThreshold.compareTo(BigDecimal.valueOf(0.0)) >= 0
                && maximumSalaryThreshold.compareTo(BigDecimal.valueOf(0.0)) > 0
                && minimumSalaryThreshold.compareTo(maximumSalaryThreshold) < 0);
    }
}
