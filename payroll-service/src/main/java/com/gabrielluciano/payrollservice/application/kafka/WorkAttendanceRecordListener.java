package com.gabrielluciano.payrollservice.application.kafka;

import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WorkAttendanceRecordListener {

    @KafkaListener(id = "${service.instanceId}", idIsGroup = false, topics = "${kafka.topicName}")
    public void listen(WorkAttendanceRecord record) {
        // TODO: Implement logic to save record
        log.info(String.valueOf(record));
    }
}
