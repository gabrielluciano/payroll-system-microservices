package com.gabrielluciano.workattendanceservice.domain.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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
    private Integer expectedWorkingHours;
    private Integer actualWorkingHours;
}
