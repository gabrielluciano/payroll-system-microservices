package com.gabrielluciano.employeeservice.domain.service.impl;

import com.gabrielluciano.employeeservice.domain.dto.CreateEmployeeRequest;
import com.gabrielluciano.employeeservice.domain.dto.EmployeeResponse;
import com.gabrielluciano.employeeservice.domain.exception.EntityNotFoundException;
import com.gabrielluciano.employeeservice.domain.model.Employee;
import com.gabrielluciano.employeeservice.domain.model.Position;
import com.gabrielluciano.employeeservice.domain.service.EmployeeService;
import com.gabrielluciano.employeeservice.infra.repository.EmployeeRepository;
import com.gabrielluciano.employeeservice.infra.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    @Override
    @Transactional
    public EmployeeResponse save(CreateEmployeeRequest createEmployeeRequest) {
        Position position = positionRepository.findById(createEmployeeRequest.positionId())
                .orElseThrow(() -> new EntityNotFoundException(createEmployeeRequest.positionId(), Position.class));

        Employee employee = createEmployeeRequest.toModel();
        employee.setPosition(position);
        return EmployeeResponse.fromModel(employeeRepository.save(employee));
    }
}
