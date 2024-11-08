package com.gabrielluciano.payrollservice.application.kafka;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import com.gabrielluciano.payrollservice.application.config.KafkaConfig;
import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.payrollservice.domain.service.PayrollService;

@ImportAutoConfiguration(KafkaAutoConfiguration.class)
@DirtiesContext
@SpringBootTest(classes = { WorkAttendanceRecordListener.class, PayrollService.class, KafkaConfig.class })
@EmbeddedKafka(partitions = 1, topics = {"${kafka.topicName}"},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class WorkAttendanceRecordListenerTest {

    private static final String VALID_CPF = "127.361.540-96";

    @Value("${kafka.topicName}")
    private String topicName;

    @Autowired
    private KafkaTemplate<String, WorkAttendanceRecord> kafkaTemplate;

    @MockBean
    private PayrollService payrollService;

    @Test
    @DisplayName("Should receive work attendance record")
    void shouldReceiveWorkAttendanceRecord() throws ExecutionException, InterruptedException {
        WorkAttendanceRecord record = new WorkAttendanceRecord(VALID_CPF, 2024, 11, 160, 150);

        kafkaTemplate.send(topicName, VALID_CPF, record).get();
        Thread.sleep(2000); // Ensure record is received

        verify(payrollService, times(1)).processPayroll(ArgumentMatchers.any());
    }
}
