package com.gabrielluciano.workattendanceservice.domain.service;

import com.gabrielluciano.workattendanceservice.domain.dto.CreateWorkAttendanceRequest;
import com.gabrielluciano.workattendanceservice.domain.dto.WorkAttendanceResponse;

public interface WorkAttendanceService {

    void save(CreateWorkAttendanceRequest createWorkAttendanceRequest);

    WorkAttendanceResponse findByCpfYearAndMonth(String cpf, Integer year, Integer month);
}
