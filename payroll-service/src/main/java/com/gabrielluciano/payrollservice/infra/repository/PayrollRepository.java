package com.gabrielluciano.payrollservice.infra.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gabrielluciano.payrollservice.domain.model.Payroll;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    @Query("FROM Payroll WHERE employeeCpf = ?1 AND year = ?2 AND month = ?3")
    Optional<Payroll> findPayroll(String employeeCpf, Integer year, Integer month);
}
