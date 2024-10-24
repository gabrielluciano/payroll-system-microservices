package com.gabrielluciano.payrollservice.domain.dto;

import java.math.BigDecimal;

import com.gabrielluciano.payrollservice.domain.model.Payroll;

public record PayrollResponse(
    Long id,
    String employeeCpf,
    Integer year,
    Integer month,
    BigDecimal grossPay,
    BigDecimal netPay
) {

    public static PayrollResponse fromModel(Payroll payroll) {
        return new PayrollResponse(payroll.getId(), payroll.getEmployeeCpf(), payroll.getYear(),
            payroll.getMonth(), payroll.getGrossPay(), payroll.getNetPay());
    }
}
