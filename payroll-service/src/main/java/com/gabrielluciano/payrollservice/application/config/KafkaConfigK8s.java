package com.gabrielluciano.payrollservice.application.config;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableKafka
@Slf4j
@Profile("k8s")
public class KafkaConfigK8s {

    @Value("${service.instanceId}")
    private String instanceId;

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${kafka.trustedPackages.workAttendancePublishService}")
    private String workAttendancePublishServicePackage;


    @Bean
    public KafkaTemplate<String, WorkAttendanceRecord> kafkaTemplate(ProducerFactory<String, WorkAttendanceRecord> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
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

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, WorkAttendanceRecord> kafkaListenerContainerFactory(
            ConsumerFactory<String, WorkAttendanceRecord> consumerFactory,
            KafkaTemplate<String, WorkAttendanceRecord> template) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, WorkAttendanceRecord>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(new DefaultErrorHandler(new DeadLetterPublishingRecoverer(template, DESTINATION_RESOLVER), new FixedBackOff(0L, 9)));
        return factory;
    }

    private static final BiFunction<ConsumerRecord<?, ?>, Exception, TopicPartition> DESTINATION_RESOLVER = (cr, e) -> {
        log.error(
            "Failed to persist record. Sending to Dead Letter Queue. " +
            "Topic: {}, Partition: {}, Offset: {}, Key: {}, Value: {}, Exception: {} - {}",
            cr.topic(), cr.partition(), cr.offset(), cr.key(), cr.value(), e.getClass().getSimpleName(), e.getMessage(), e);

        return new TopicPartition(cr.topic() + ".DLT", cr.partition());
    };

    @Bean
    public ConsumerFactory<String, WorkAttendanceRecord> consumerFactory(
        @Value("${kafka.sasl.username}") String saslUsername,
        @Value("${kafka.sasl.password}") String saslPassword,
        @Value("${kafka.bootstrapServers}") String bootstrapServers
    ) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, applicationName);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, workAttendancePublishServicePackage);
        props.put(JsonDeserializer.TYPE_MAPPINGS, "work-attendance-record:" + WorkAttendanceRecord.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // SASL Authentication
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SASL_PLAINTEXT");
        props.put(SaslConfigs.SASL_MECHANISM, "SCRAM-SHA-256");
        props.put(SaslConfigs.SASL_JAAS_CONFIG, String.format(
            "org.apache.kafka.common.security.scram.ScramLoginModule required username=\"%s\" password=\"%s\";",
            saslUsername, saslPassword)
        );
        return new DefaultKafkaConsumerFactory<>(props);
    }
}

