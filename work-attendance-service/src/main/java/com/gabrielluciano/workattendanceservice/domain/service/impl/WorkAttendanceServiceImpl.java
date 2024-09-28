package com.gabrielluciano.workattendanceservice.domain.service.impl;

import com.gabrielluciano.workattendanceservice.domain.dto.CreateWorkAttendanceRequest;
import com.gabrielluciano.workattendanceservice.domain.dto.WorkAttendanceResponse;
import com.gabrielluciano.workattendanceservice.domain.exception.DuplicatedEntityException;
import com.gabrielluciano.workattendanceservice.domain.exception.EntityNotFoundException;
import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecordId;
import com.gabrielluciano.workattendanceservice.domain.service.WorkAttendanceService;
import com.gabrielluciano.workattendanceservice.infra.repository.WorkAttendanceRecordRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkAttendanceServiceImpl implements WorkAttendanceService {

    private final WorkAttendanceRecordRepository repository;

    @Override
    @Transactional
    public void save(@Valid CreateWorkAttendanceRequest createWorkAttendanceRequest) {
        var record = createWorkAttendanceRequest.toModel();
        var recordFromDb = repository.findByRecordId(record.getRecordId());
        if (recordFromDb.isPresent())
            throw new DuplicatedEntityException(record.getRecordId(), WorkAttendanceRecord.class);

        repository.save(createWorkAttendanceRequest.toModel());
    }

    @Override
    public WorkAttendanceResponse findByCpfYearAndMonth(String cpf, Integer year, Integer month) {
        var recordId = new WorkAttendanceRecordId(cpf, year, month);
        return repository.findByRecordId(recordId)
                .map(WorkAttendanceResponse::fromModel)
                .orElseThrow(() -> new EntityNotFoundException(recordId, WorkAttendanceRecord.class));
    }
}
