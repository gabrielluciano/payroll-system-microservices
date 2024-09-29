package com.gabrielluciano.workattendancepublishservice.domain.service.impl;

import com.gabrielluciano.workattendancepublishservice.domain.dto.CreateWorkAttendanceRequest;
import com.gabrielluciano.workattendancepublishservice.domain.service.WorkAttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkAttendanceServiceImpl implements WorkAttendanceService {

    @Override
    @Transactional
    public void save(@Valid CreateWorkAttendanceRequest createWorkAttendanceRequest) {
        var record = createWorkAttendanceRequest.toModel();
        // TODO: publish event to Kafka
        System.out.println("publish event to Kafka");
    }
}
