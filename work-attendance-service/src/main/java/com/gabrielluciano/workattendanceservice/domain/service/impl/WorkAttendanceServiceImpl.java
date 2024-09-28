package com.gabrielluciano.workattendanceservice.domain.service.impl;

import com.gabrielluciano.workattendanceservice.domain.dto.CreateWorkAttendanceRequest;
import com.gabrielluciano.workattendanceservice.domain.exception.DuplicatedEntityException;
import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.workattendanceservice.domain.service.WorkAttendanceService;
import com.gabrielluciano.workattendanceservice.infra.repository.WorkAttendanceRecordRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkAttendanceServiceImpl implements WorkAttendanceService {

    private final WorkAttendanceRecordRepository repository;

    @Override
    @Transactional
    public void save(@Valid CreateWorkAttendanceRequest createWorkAttendanceRequest) {
        var record = createWorkAttendanceRequest.toModel();
        Optional<WorkAttendanceRecord> recordFromDb = repository.findByRecordId(record.getRecordId());
        if (recordFromDb.isPresent())
            throw new DuplicatedEntityException(record.getRecordId(), WorkAttendanceRecord.class);

        repository.save(createWorkAttendanceRequest.toModel());
    }
}
