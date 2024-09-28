package com.gabrielluciano.workattendanceservice.domain.service;

import com.gabrielluciano.workattendanceservice.domain.dto.CreateWorkAttendanceRequest;

public interface WorkAttendanceService {

    void save(CreateWorkAttendanceRequest createWorkAttendanceRequest);
}
