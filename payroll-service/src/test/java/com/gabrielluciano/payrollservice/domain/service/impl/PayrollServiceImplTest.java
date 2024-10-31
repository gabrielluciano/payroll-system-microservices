package com.gabrielluciano.payrollservice.domain.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.gabrielluciano.payrollservice.domain.calculation.IncomeDiscountCalculator;
import com.gabrielluciano.payrollservice.domain.calculation.InssDiscountCalculator;
import com.gabrielluciano.payrollservice.domain.dto.EmployeePosition;
import com.gabrielluciano.payrollservice.domain.dto.EmployeeResponse;
import com.gabrielluciano.payrollservice.domain.model.Payroll;
import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.payrollservice.domain.service.EmployeeService;
import com.gabrielluciano.payrollservice.infra.repository.PayrollRepository;

@ExtendWith(SpringExtension.class)
class PayrollServiceImplTest {

    @MockBean
    private PayrollRepository payrollRepository;
    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private InssDiscountCalculator inssDiscountCalculator;
    @MockBean
    private IncomeDiscountCalculator incomeDiscountCalculator;

    private PayrollServiceImpl payrollServiceImpl;

    @BeforeEach
    void setUp() {
        payrollServiceImpl = new PayrollServiceImpl(payrollRepository, 
            employeeService, inssDiscountCalculator, incomeDiscountCalculator);
    }

    @ParameterizedTest
    @CsvSource({
        "3500.00, 100.00, 200.00, 100, 100, 3500.00, 3200.00",
        "3500.00, 100.00, 200.00, 80, 100, 2800.00, 2500.00",
        "3500.00, 0.00, 0.00, 0, 100, 0.00, 0.00",
    })
    void testCreatePayrollObj(
        String salary,
        String inssDiscount,
        String incomeDiscount,
        Integer actualWorkingHours,
        Integer expectedWorkingHours,
        String expectedGrossPay,
        String expectedNetPay
    ) {
        final String VALID_CPF = "127.361.540-96";
        EmployeeResponse employee = new EmployeeResponse("Test", VALID_CPF, new BigDecimal(salary), new EmployeePosition(1L, "Developer"));
        when(employeeService.findByCpf(anyString())).thenReturn(employee);
        when(incomeDiscountCalculator.calculateDiscount(any())).thenReturn(new BigDecimal(inssDiscount));
        when(inssDiscountCalculator.calculateDiscount(any())).thenReturn(new BigDecimal(incomeDiscount));

        var attendanceRecord = new WorkAttendanceRecord(VALID_CPF, 2020, 1, expectedWorkingHours, actualWorkingHours);

        Payroll payroll = payrollServiceImpl.createPayrollObj(attendanceRecord);

        assertThat(payroll.getGrossPay()).isEqualByComparingTo(expectedGrossPay);
        assertThat(payroll.getNetPay()).isEqualByComparingTo(expectedNetPay);
    }
}
