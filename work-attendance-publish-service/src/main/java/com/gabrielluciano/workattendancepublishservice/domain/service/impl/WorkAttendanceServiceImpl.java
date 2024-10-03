package com.gabrielluciano.workattendancepublishservice.domain.service.impl;

import com.gabrielluciano.workattendancepublishservice.domain.dto.CreateWorkAttendanceRequest;
import com.gabrielluciano.workattendancepublishservice.domain.exception.EntityNotFoundException;
import com.gabrielluciano.workattendancepublishservice.domain.exception.InternalServerErrorException;
import com.gabrielluciano.workattendancepublishservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.workattendancepublishservice.domain.service.EmployeeService;
import com.gabrielluciano.workattendancepublishservice.domain.service.WorkAttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkAttendanceServiceImpl implements WorkAttendanceService {

    private final KafkaTemplate<String, WorkAttendanceRecord> kafkaTemplate;
    private final EmployeeService employeeService;

    @Override
    public void save(@Valid CreateWorkAttendanceRequest createWorkAttendanceRequest) {
        final var record = createWorkAttendanceRequest.toModel();

        if (!employeeService.existsByCpf(record.getEmployeeCpf()))
            throw new EntityNotFoundException(record.getEmployeeCpf(), "Employee");

        try {
            kafkaTemplate.sendDefault(record.getEmployeeCpf(), record).get();
        } catch (ExecutionException ex) {
            throw new InternalServerErrorException("Error publishing event to Kafka", ex);
        } catch (InterruptedException ex) {
            log.error("The thread was interrupted", ex);
            Thread.currentThread().interrupt();
        }
    }
}
