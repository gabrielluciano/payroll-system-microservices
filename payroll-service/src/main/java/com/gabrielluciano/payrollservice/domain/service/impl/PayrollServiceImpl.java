package com.gabrielluciano.payrollservice.domain.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabrielluciano.payrollservice.domain.calculation.IncomeDiscountCalculator;
import com.gabrielluciano.payrollservice.domain.calculation.InssDiscountCalculator;
import com.gabrielluciano.payrollservice.domain.dto.EmployeeResponse;
import com.gabrielluciano.payrollservice.domain.dto.PayrollResponse;
import com.gabrielluciano.payrollservice.domain.model.Payroll;
import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.payrollservice.domain.service.EmployeeService;
import com.gabrielluciano.payrollservice.domain.service.PayrollService;
import com.gabrielluciano.payrollservice.domain.service.exception.EntityNotFoundException;
import com.gabrielluciano.payrollservice.infra.repository.PayrollRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PayrollServiceImpl implements PayrollService {

    private final PayrollRepository repository;
    private final EmployeeService employeeService;
    private final InssDiscountCalculator inssDiscountCalculator;
    private final IncomeDiscountCalculator incomeDiscountCalculator;

    @Override
    @Transactional
    public void processPayroll(WorkAttendanceRecord attendanceRecord) {
        if (attendanceRecord == null) {
            log.warn("null WorkAttendanceRecord detected. Skiping processing");
            return;
        }

        final var optionalPayroll = repository.findPayroll(
            attendanceRecord.getEmployeeCpf(), attendanceRecord.getYear(), attendanceRecord.getMonth());

        if (optionalPayroll.isPresent()) {
            log.warn("Payroll with for cpf '{}', year '{}' and month '{}' already exists. Skipping processing",
                attendanceRecord.getEmployeeCpf(), attendanceRecord.getYear(), attendanceRecord.getMonth());
            return;
        }

        final Payroll payroll = createPayrollObj(attendanceRecord);

        repository.save(payroll);
    }

    protected Payroll createPayrollObj(WorkAttendanceRecord attendanceRecord) {
        final EmployeeResponse employee = employeeService.findByCpf(attendanceRecord.getEmployeeCpf());
        final BigDecimal grossPay = employee.baseSalary().multiply(attendanceRecord.getPercentageOfHoursWorked());

        final BigDecimal inssDiscount = inssDiscountCalculator.calculateDiscount(grossPay);
        final BigDecimal incomeDiscount = incomeDiscountCalculator.calculateDiscount(grossPay.subtract(inssDiscount));

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

    @Override
    public PayrollResponse getPayroll(String cpf, Integer year, Integer month) {
        return repository.findPayroll(cpf, year, month)
                    .map(PayrollResponse::fromModel)
                    .orElseThrow(() -> new EntityNotFoundException(
                        String.format("%s,%d,%d", cpf, year, month), Payroll.class));
    }
}

