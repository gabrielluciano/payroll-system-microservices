package com.gabrielluciano.payrollservice.application.config;

import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${kafka.trustedPackages.workAttendancePublishService}")
    private String workAttendancePublishServicePackage;

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, WorkAttendanceRecord> kafkaListenerContainerFactory(
            ConsumerFactory<String, WorkAttendanceRecord> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, WorkAttendanceRecord>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, WorkAttendanceRecord> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerProps());
    }

    private Map<String, Object> consumerProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, applicationName);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, workAttendancePublishServicePackage);
        props.put(JsonDeserializer.TYPE_MAPPINGS, "work-attendance-record:" + WorkAttendanceRecord.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }
}
