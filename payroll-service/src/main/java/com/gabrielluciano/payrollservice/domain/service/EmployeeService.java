package com.gabrielluciano.payrollservice.domain.service;

import com.gabrielluciano.payrollservice.domain.dto.EmployeeResponse;
import com.gabrielluciano.payrollservice.domain.service.exception.EntityNotFoundException;

public interface EmployeeService {

    EmployeeResponse findByCpf(String cpf) throws EntityNotFoundException;

}
