package com.gabrielluciano.insstaxservice.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
    @Column(nullable = false, unique = true, precision = 10, scale = 2)
    private BigDecimal maximumSalaryThreshold;
    @Column(nullable = false, precision = 4, scale = 2)
    private BigDecimal rate;

    @OneToOne
    @JsonIgnore
    private TaxRate previousTaxRate;

    @Override
    public int compareTo(TaxRate o) {
        return maximumSalaryThreshold.compareTo(o.maximumSalaryThreshold);
    }
}
