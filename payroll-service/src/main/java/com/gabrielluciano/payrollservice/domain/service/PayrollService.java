package com.gabrielluciano.payrollservice.domain.service;

import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;

public interface PayrollService {

    void processPayroll(WorkAttendanceRecord record);
}