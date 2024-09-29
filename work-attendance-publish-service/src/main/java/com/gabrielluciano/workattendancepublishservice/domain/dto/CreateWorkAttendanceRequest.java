package com.gabrielluciano.workattendancepublishservice.domain.dto;

import com.gabrielluciano.workattendancepublishservice.domain.model.WorkAttendanceRecord;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record CreateWorkAttendanceRequest(
        @NotBlank @CPF String employeeCpf,
        @NotNull @Min(1970) Integer year,
        @NotNull @Min(1) @Max(12) Integer month,
        @NotNull @Min(0) Integer expectedWorkingHours,
        @NotNull @Min(0) Integer actualWorkingHours
) {
    public WorkAttendanceRecord toModel() {
        return new WorkAttendanceRecord(employeeCpf, year, month, expectedWorkingHours, actualWorkingHours);
    }
}
