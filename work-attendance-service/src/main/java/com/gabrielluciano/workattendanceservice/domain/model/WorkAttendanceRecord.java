package com.gabrielluciano.workattendanceservice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "work_attendance_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkAttendanceRecord {

    @EmbeddedId
    private WorkAttendanceRecordId recordId;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer expectedWorkingHours;

    @Column(nullable = false)
    @NotNull
    @Min(0)
    private Integer actualWorkingHours;
}
