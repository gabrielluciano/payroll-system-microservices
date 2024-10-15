package com.gabrielluciano.payrollservice.domain.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabrielluciano.payrollservice.domain.dto.Employee;
import com.gabrielluciano.payrollservice.domain.model.Payroll;
import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.payrollservice.domain.service.EmployeeService;
import com.gabrielluciano.payrollservice.domain.service.IncomeService;
import com.gabrielluciano.payrollservice.domain.service.InssService;
import com.gabrielluciano.payrollservice.domain.service.PayrollService;
import com.gabrielluciano.payrollservice.infra.repository.PayrollRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository repository;
    private final EmployeeService employeeService;
    private final InssService inssService;
    private final IncomeService incomeService;

    @Override
    @Transactional
    public void processPayroll(WorkAttendanceRecord attendanceRecord) {
        final Payroll payroll = createPayrollObj(attendanceRecord);
        repository.save(payroll);
    }

    protected Payroll createPayrollObj(WorkAttendanceRecord attendanceRecord) {
        final Employee employee = employeeService.findByCpf(attendanceRecord.getEmployeeCpf());
        final BigDecimal grossPay = employee.baseSalary().multiply(attendanceRecord.getPercentageOfHoursWorked());

        final BigDecimal inssDiscount = inssService.calculateDiscount(grossPay);
        final BigDecimal incomeDiscount = incomeService.calculateDiscount(grossPay.subtract(inssDiscount));

        final BigDecimal netPay = grossPay
            .subtract(inssDiscount)
            .subtract(incomeDiscount)
            .setScale(2, RoundingMode.HALF_UP);

        return Payroll.builder()
            .employeeCpf(employee.cpf())
            .month(attendanceRecord.getMonth())
            .year(attendanceRecord.getYear())
            .grossPay(grossPay)
            .netPay(netPay)
            .build();
    }
}
