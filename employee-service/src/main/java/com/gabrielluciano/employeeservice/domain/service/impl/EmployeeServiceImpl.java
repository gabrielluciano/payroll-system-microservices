package com.gabrielluciano.employeeservice.domain.service.impl;

import com.gabrielluciano.employeeservice.domain.dto.CreateEmployeeRequest;
import com.gabrielluciano.employeeservice.domain.dto.EmployeeResponse;
import com.gabrielluciano.employeeservice.domain.dto.UpdateEmployeeRequest;
import com.gabrielluciano.employeeservice.domain.exception.DuplicatedEntityException;
import com.gabrielluciano.employeeservice.domain.exception.EntityNotFoundException;
import com.gabrielluciano.employeeservice.domain.model.Employee;
import com.gabrielluciano.employeeservice.domain.model.Position;
import com.gabrielluciano.employeeservice.domain.service.EmployeeService;
import com.gabrielluciano.employeeservice.infra.repository.EmployeeRepository;
import com.gabrielluciano.employeeservice.infra.repository.PositionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;

    @Override
    public EmployeeResponse findByCpf(String cpf) throws EntityNotFoundException {
        return employeeRepository.findByCpf(cpf)
                .map(EmployeeResponse::fromModel)
                .orElseThrow(() -> new EntityNotFoundException(cpf, Employee.class));
    }

    @Override
    @Transactional
    public EmployeeResponse save(@Valid CreateEmployeeRequest createEmployeeRequest) {
        var employeeFromDb = employeeRepository.findByCpf(createEmployeeRequest.cpf());
        if (employeeFromDb.isPresent())
            throw new DuplicatedEntityException(createEmployeeRequest.cpf(), Employee.class);

        Position position = positionRepository.findById(createEmployeeRequest.positionId())
                .orElseThrow(() -> new EntityNotFoundException(createEmployeeRequest.positionId(), Position.class));

        Employee employee = createEmployeeRequest.toModel();
        employee.setPosition(position);
        return EmployeeResponse.fromModel(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeResponse update(String cpf, @Valid UpdateEmployeeRequest updateEmployeeRequest) {
        Employee employee = employeeRepository.findByCpf(cpf)
                .orElseThrow(() -> new EntityNotFoundException(cpf, Employee.class));
        Position position = positionRepository.findById(updateEmployeeRequest.positionId())
                .orElseThrow(() -> new EntityNotFoundException(updateEmployeeRequest.positionId(), Position.class));

        employee.setName(updateEmployeeRequest.name());
        employee.setBaseSalary(updateEmployeeRequest.baseSalary());
        employee.setPosition(position);

        return EmployeeResponse.fromModel(employeeRepository.save(employee));
    }
}
