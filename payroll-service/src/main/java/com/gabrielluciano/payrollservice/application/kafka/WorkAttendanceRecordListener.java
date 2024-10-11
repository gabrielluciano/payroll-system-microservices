package com.gabrielluciano.payrollservice.application.kafka;

import com.gabrielluciano.payrollservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.payrollservice.domain.service.PayrollService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class WorkAttendanceRecordListener {

    private final PayrollService payrollService;

    @KafkaListener(id = "${service.instanceId}", idIsGroup = false, topics = "${kafka.topicName}")
    public void listen(WorkAttendanceRecord record) {
        payrollService.processPayroll(record);
    }
}
