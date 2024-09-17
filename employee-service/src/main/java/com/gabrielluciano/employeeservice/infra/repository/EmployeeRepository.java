package com.gabrielluciano.employeeservice.infra.repository;

import com.gabrielluciano.employeeservice.domain.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Optional<Employee> findByCpf(String cpf);
}
