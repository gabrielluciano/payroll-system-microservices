package com.gabrielluciano.payrollservice.domain.model;

import static java.math.BigDecimal.valueOf;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkAttendanceRecord {

    @NotBlank
    @CPF
    private String employeeCpf;

    @NotNull
    @Min(1970)
    private Integer year;

    @NotNull
    @Min(1)
    @Max(12)
    private Integer month;

    @NotNull
    @Min(0)
    private Integer expectedWorkingHours;

    @NotNull
    @Min(0)
    private Integer actualWorkingHours;

    @JsonIgnore
    public BigDecimal getPercentageOfHoursWorked() {
        if (actualWorkingHours <= 0)
            return BigDecimal.ZERO;

        return valueOf(actualWorkingHours).divide(valueOf(expectedWorkingHours), new MathContext(5, RoundingMode.HALF_UP));
    }
}
