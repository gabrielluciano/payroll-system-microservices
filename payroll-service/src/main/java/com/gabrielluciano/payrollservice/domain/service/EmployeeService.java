package com.gabrielluciano.payrollservice.domain.service;

import com.gabrielluciano.payrollservice.domain.dto.Employee;
import com.gabrielluciano.payrollservice.domain.service.exception.EntityNotFoundException;

public interface EmployeeService {

    Employee findByCpf(String cpf) throws EntityNotFoundException;

}
