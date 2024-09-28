package com.gabrielluciano.workattendanceservice.infra.repository;

import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecordId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkAttendanceRecordRepository extends JpaRepository<WorkAttendanceRecord, WorkAttendanceRecordId> {

    Optional<WorkAttendanceRecord> findByRecordId(WorkAttendanceRecordId recordId);
}
