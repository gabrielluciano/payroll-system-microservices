package com.gabrielluciano.payrollservice.domain.service.impl;

import com.gabrielluciano.payrollservice.domain.model.Payroll;
import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.payrollservice.domain.service.PayrollService;
import com.gabrielluciano.payrollservice.infra.repository.PayrollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository repository;

    @Override
    @Transactional
    public void processPayroll(WorkAttendanceRecord record) {
        final BigDecimal temporaryFixedSalary = BigDecimal.valueOf(2000.00);

        // TODO: Implement payroll calculation logic here

        Payroll payroll = new Payroll();
        payroll.setYear(record.getYear());
        payroll.setMonth(record.getMonth());
        payroll.setEmployeeCpf(record.getEmployeeCpf());
        payroll.setGrossPay(temporaryFixedSalary);
        payroll.setNetPay(temporaryFixedSalary);

        repository.save(payroll);
    }
}
