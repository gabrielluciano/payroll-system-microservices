package com.gabrielluciano.employeeservice.domain.service;

import com.gabrielluciano.employeeservice.domain.dto.CreateEmployeeRequest;
import com.gabrielluciano.employeeservice.domain.dto.EmployeeResponse;

public interface EmployeeService {

    EmployeeResponse save(CreateEmployeeRequest createEmployeeRequest);
}
