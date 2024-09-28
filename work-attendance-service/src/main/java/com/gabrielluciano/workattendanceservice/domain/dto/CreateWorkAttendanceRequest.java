package com.gabrielluciano.workattendanceservice.domain.dto;

import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecordId;
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
        var id = new WorkAttendanceRecordId(employeeCpf, year, month);
        return new WorkAttendanceRecord(id, expectedWorkingHours, actualWorkingHours);
    }
}
