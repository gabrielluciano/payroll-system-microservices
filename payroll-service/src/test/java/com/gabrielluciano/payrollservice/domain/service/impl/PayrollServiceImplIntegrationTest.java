package com.gabrielluciano.payrollservice.domain.service.impl;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

import com.gabrielluciano.payrollservice.application.kafka.WorkAttendanceRecordListener;
import com.gabrielluciano.payrollservice.config.TestcontainersConfiguration;
import com.gabrielluciano.payrollservice.domain.calculation.IncomeDiscountCalculator;
import com.gabrielluciano.payrollservice.domain.calculation.InssDiscountCalculator;
import com.gabrielluciano.payrollservice.domain.dto.Employee;
import com.gabrielluciano.payrollservice.domain.dto.EmployeePosition;
import com.gabrielluciano.payrollservice.domain.model.Payroll;
import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.payrollservice.domain.service.EmployeeService;
import com.gabrielluciano.payrollservice.infra.repository.PayrollRepository;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(properties = {"eureka.client.enabled=false", "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"})
class PayrollServiceImplIntegrationTest {

    private static final String VALID_CPF = "127.361.540-96";

    // Mock Kafka Related Beans
    @MockBean
    ConcurrentKafkaListenerContainerFactory<String, WorkAttendanceRecord> kafkaListenerContainerFactory;
    @MockBean
    public ConsumerFactory<String, WorkAttendanceRecord> consumerFactory;
    @MockBean
    private WorkAttendanceRecordListener listener;

    @MockBean
    private EmployeeService employeeService;
    @MockBean
    private InssDiscountCalculator inssDiscountCalculator;
    @MockBean
    private IncomeDiscountCalculator incomeDiscountCalculator;

    @Autowired
    private PayrollServiceImpl payrollService;

    @Autowired
    private PayrollRepository payrollRepository;

    @BeforeEach
    void setUp() {
        payrollRepository.deleteAll();

        Employee employee = new Employee("Test", VALID_CPF, valueOf(3500.00), new EmployeePosition(1L, "Developer"));
        when(employeeService.findByCpf(anyString())).thenReturn(employee);
        when(incomeDiscountCalculator.calculateDiscount(any())).thenReturn(valueOf(100.00));
        when(inssDiscountCalculator.calculateDiscount(any())).thenReturn(valueOf(200.00));
    }

    @Test
    @DisplayName("Should skip processing if attendanceRecord is null")
    void shouldSkipProcessingIfAttendanceRecordIsNull() {
        payrollService.processPayroll(null);
        assertThat(payrollRepository.count()).isZero();
    }

    @Test
    @DisplayName("Should save payroll")
    void shouldSavePayroll() {
        WorkAttendanceRecord record = new WorkAttendanceRecord(VALID_CPF, 2024, 11, 160, 150);

        payrollService.processPayroll(record);

        assertThat(payrollRepository.count()).isOne();
    }

    @Test
    @DisplayName("Should not save payroll when duplicated cpf, month and year")
    void shouldNotSavePayrollWhenDuplcatedCpfMonthAndYear() {
        int year = 2024;
        int month = 11;
        Payroll payroll = new Payroll(null, VALID_CPF, year, month, valueOf(3500.00), valueOf(3300.00));
        payrollRepository.saveAndFlush(payroll);

        WorkAttendanceRecord record = new WorkAttendanceRecord(VALID_CPF, year, month, 150, 150);

        payrollService.processPayroll(record);

        assertThat(payrollRepository.count()).isOne();
    }
}
