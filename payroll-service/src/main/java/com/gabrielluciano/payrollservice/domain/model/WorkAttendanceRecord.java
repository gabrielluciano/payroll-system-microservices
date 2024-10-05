package com.gabrielluciano.payrollservice.domain.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

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
}
