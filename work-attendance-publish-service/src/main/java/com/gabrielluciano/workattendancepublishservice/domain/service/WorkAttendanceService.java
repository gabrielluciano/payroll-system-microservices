package com.gabrielluciano.workattendancepublishservice.domain.service;

import com.gabrielluciano.workattendancepublishservice.domain.dto.CreateWorkAttendanceRequest;

public interface WorkAttendanceService {

    void save(CreateWorkAttendanceRequest createWorkAttendanceRequest);
}
