package com.gabrielluciano.employeeservice.domain.service;

import com.gabrielluciano.employeeservice.domain.dto.CreateEmployeeRequest;
import com.gabrielluciano.employeeservice.domain.dto.EmployeeResponse;
import com.gabrielluciano.employeeservice.domain.exception.EntityNotFoundException;

public interface EmployeeService {

    EmployeeResponse findByCpf(String cpf) throws EntityNotFoundException;

    EmployeeResponse save(CreateEmployeeRequest createEmployeeRequest);
}
