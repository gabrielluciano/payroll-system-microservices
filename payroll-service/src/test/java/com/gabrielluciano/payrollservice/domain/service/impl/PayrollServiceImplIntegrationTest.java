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
import com.gabrielluciano.payrollservice.domain.dto.Employee;
import com.gabrielluciano.payrollservice.domain.dto.EmployeePosition;
import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.payrollservice.domain.service.EmployeeService;
import com.gabrielluciano.payrollservice.domain.service.IncomeService;
import com.gabrielluciano.payrollservice.domain.service.InssService;
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
    private InssService inssService;
    @MockBean
    private IncomeService incomeService;

    @Autowired
    private PayrollServiceImpl payrollService;

    @Autowired
    private PayrollRepository payrollRepository;

    @BeforeEach
    void setUp() {
        payrollRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save payroll")
    void shouldSavePayroll() {
        Employee employee = new Employee("Test", VALID_CPF, valueOf(3500.00), new EmployeePosition(1L, "Developer"));
        when(employeeService.findByCpf(anyString())).thenReturn(employee);
        when(incomeService.calculateDiscount(any())).thenReturn(valueOf(100.00));
        when(inssService.calculateDiscount(any())).thenReturn(valueOf(200.00));

        WorkAttendanceRecord record = new WorkAttendanceRecord(VALID_CPF, 2024, 11, 160, 150);

        payrollService.processPayroll(record);

        assertThat(payrollRepository.count()).isOne();
    }
}
