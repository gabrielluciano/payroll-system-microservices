package com.gabrielluciano.workattendancepublishservice.kafka;

import com.gabrielluciano.workattendancepublishservice.domain.dto.CreateWorkAttendanceRequest;
import com.gabrielluciano.workattendancepublishservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.workattendancepublishservice.domain.service.EmployeeService;
import com.gabrielluciano.workattendancepublishservice.util.JsonUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"eureka.client.enabled=false"})
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, topics = {WorkAttendanceRecordKafkaTest.TOPIC_NAME},
        brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class WorkAttendanceRecordKafkaTest {

    public static final String TOPIC_NAME = "work-attendance-events";
    private static final String VALID_CPF = "127.361.540-96";

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should publish work attendance record to kafka")
    void shouldPublishSaveWorkAttendanceRecordToKafka() throws Exception {
        when(employeeService.existsByCpf(ArgumentMatchers.anyString()))
                .thenReturn(true);

        var request = new CreateWorkAttendanceRequest(VALID_CPF, 2024, 10, 180, 180);

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        var records = getKafkaRecords();

        assertThat(records.size()).isOne();
        assertThat(records.get(0).key()).isEqualTo(request.employeeCpf());

        WorkAttendanceRecord record = records.get(0).value();
        assertThat(record.getEmployeeCpf()).isEqualTo(request.employeeCpf());
        assertThat(record.getYear()).isEqualTo(request.year());
        assertThat(record.getMonth()).isEqualTo(request.month());
        assertThat(record.getActualWorkingHours()).isEqualTo(request.actualWorkingHours());
        assertThat(record.getExpectedWorkingHours()).isEqualTo(request.expectedWorkingHours());
    }

    private List<ConsumerRecord<String, WorkAttendanceRecord>> getKafkaRecords() throws Exception {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", embeddedKafka);
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProps.put(JsonDeserializer.TYPE_MAPPINGS, "work-attendance-record:" + WorkAttendanceRecord.class.getName());
        ConsumerFactory<String, WorkAttendanceRecord> cf = new DefaultKafkaConsumerFactory<>(consumerProps);
        Consumer<String, WorkAttendanceRecord> consumer = cf.createConsumer();

        Thread.sleep(50);

        this.embeddedKafka.consumeFromAnEmbeddedTopic(consumer, TOPIC_NAME);
        ConsumerRecords<String, WorkAttendanceRecord> replies = KafkaTestUtils.getRecords(consumer);
        return StreamSupport.stream(replies.spliterator(), false).toList();
    }
}
