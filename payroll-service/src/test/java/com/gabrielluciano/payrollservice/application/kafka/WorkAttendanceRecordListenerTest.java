package com.gabrielluciano.payrollservice.application.kafka;

import com.gabrielluciano.payrollservice.application.config.KafkaConfig;
import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.payrollservice.domain.service.PayrollService;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ImportAutoConfiguration(KafkaAutoConfiguration.class)
@SpringBootTest(classes = {
        WorkAttendanceRecordListener.class, PayrollService.class, KafkaConfig.class, WorkAttendanceRecordListenerTest.KafkaTestConfiguration.class})
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
        Thread.sleep(1000); // Ensure record is received

        verify(payrollService, times(1)).processPayroll(ArgumentMatchers.any());
    }

    @TestConfiguration
    @EnableKafka
    static class KafkaTestConfiguration {

        @Bean
        public KafkaTemplate<String, WorkAttendanceRecord> kafkaTemplate(ProducerFactory<String, WorkAttendanceRecord> producerFactory) {
            return new KafkaTemplate<>(producerFactory);
        }

        @Bean
        public ProducerFactory<String, WorkAttendanceRecord> producerFactory() {
            Map<String, Object> props = new HashMap<>();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            props.put(ProducerConfig.ACKS_CONFIG, "-1");
            props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");
            props.put(ProducerConfig.CLIENT_ID_CONFIG, "test");
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
            props.put(JsonSerializer.TYPE_MAPPINGS, "work-attendance-record:" + WorkAttendanceRecord.class.getName());
            return new DefaultKafkaProducerFactory<>(props);
        }
    }
}
