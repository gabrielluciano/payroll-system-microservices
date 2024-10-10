package com.gabrielluciano.payrollservice.infra.repository;

import com.gabrielluciano.payrollservice.domain.model.Payroll;
import org.springframework.data.repository.CrudRepository;

public interface PayrollRepository extends CrudRepository<Payroll, Long> {
}
