package com.gabrielluciano.employeeservice.infra.repository;

import com.gabrielluciano.employeeservice.domain.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
}
