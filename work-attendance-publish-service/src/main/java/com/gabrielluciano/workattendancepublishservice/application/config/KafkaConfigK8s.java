package com.gabrielluciano.workattendancepublishservice.application.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.gabrielluciano.workattendancepublishservice.domain.model.WorkAttendanceRecord;

@Configuration
@EnableKafka
@Profile("k8s")
public class KafkaConfigK8s {

    @Value("${service.instanceId}")
    private String instanceId;

    @Value("${kafka.topicName}")
    private String topicName;

    @Bean
    public KafkaTemplate<String, WorkAttendanceRecord> kafkaTemplate(ProducerFactory<String, WorkAttendanceRecord> producerFactory) {
        var kafkaTemplate = new KafkaTemplate<>(producerFactory);
        kafkaTemplate.setDefaultTopic(topicName);
        return kafkaTemplate;
    }

    @Bean
    public ProducerFactory<String, WorkAttendanceRecord> producerFactory(
        @Value("${kafka.sasl.username}") String saslUsername,
        @Value("${kafka.sasl.password}") String saslPassword,
        @Value("${kafka.bootstrapServers}") String bootstrapServers
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.ACKS_CONFIG, "-1");
        props.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "zstd");
        props.put(ProducerConfig.CLIENT_ID_CONFIG, instanceId);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(JsonSerializer.TYPE_MAPPINGS, "work-attendance-record:" + WorkAttendanceRecord.class.getName());

        // SASL Authentication
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
            "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";",
            saslUsername, saslPassword)
        );
        return new DefaultKafkaProducerFactory<>(props);
    }
}
