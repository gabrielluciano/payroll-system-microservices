package com.gabrielluciano.workattendanceservice.domain.dto;

import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecord;

public record WorkAttendanceResponse(
        String employeeCpf,
        Integer year,
        Integer month,
        Integer expectedWorkingHours,
        Integer actualWorkingHours
) {

    public static WorkAttendanceResponse fromModel(WorkAttendanceRecord record) {
        return new WorkAttendanceResponse(
                record.getRecordId().getEmployeeCpf(),
                record.getRecordId().getYear(),
                record.getRecordId().getMonth(),
                record.getExpectedWorkingHours(),
                record.getActualWorkingHours()
        );
    }
}
