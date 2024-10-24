package com.gabrielluciano.payrollservice.application.resource;

import static java.math.BigDecimal.valueOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.test.web.servlet.MockMvc;

import com.gabrielluciano.payrollservice.application.kafka.WorkAttendanceRecordListener;
import com.gabrielluciano.payrollservice.config.TestcontainersConfiguration;
import com.gabrielluciano.payrollservice.domain.model.Payroll;
import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.payrollservice.infra.repository.PayrollRepository;

@SpringBootTest(properties = {"eureka.client.enabled=false", "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
class PayrollResourceTest {

    final String VALID_CPF = "127.361.540-96";

    // Mock Kafka Related Beans
    @MockBean
    ConcurrentKafkaListenerContainerFactory<String, WorkAttendanceRecord> kafkaListenerContainerFactory;
    @MockBean
    public ConsumerFactory<String, WorkAttendanceRecord> consumerFactory;
    @MockBean
    private WorkAttendanceRecordListener listener;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PayrollRepository payrollRepository;

    @BeforeEach
    void setUp() {
        payrollRepository.deleteAll();
    }

    @Test
    @DisplayName("Should find payroll by cpf, year and month")
    void shouldFindPayrollByCpfYearAndMonth() throws Exception {
        Payroll payroll = new Payroll(null, VALID_CPF, 2024, 12, valueOf(1000.00), valueOf(900.00));
        payrollRepository.saveAndFlush(payroll);

        mockMvc.perform(get(String.format("/payrolls/%s/%d/%d", payroll.getEmployeeCpf(), payroll.getYear(), payroll.getMonth())))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCpf", equalTo(payroll.getEmployeeCpf())))
                .andExpect(jsonPath("$.year", equalTo(payroll.getYear())))
                .andExpect(jsonPath("$.month", equalTo(payroll.getMonth())));
    }

    @Test
    @DisplayName("Should not find payroll by cpf, year and month")
    void shouldNotFindPayrollByCpfYearAndMonth() throws Exception {
        Payroll payroll = new Payroll(null, VALID_CPF, 2024, 12, valueOf(1000.00), valueOf(900.00));

        mockMvc.perform(get(String.format("/payrolls/%s/%d/%d", payroll.getEmployeeCpf(), payroll.getYear(), payroll.getMonth())))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path", containsString("/payrolls/")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.error", equalTo("Entity Not Found")))
                .andExpect(jsonPath("$.message", containsString("Payroll")));
    }
}
