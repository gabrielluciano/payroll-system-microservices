package com.gabrielluciano.workattendancepublishservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class WorkAttendancePublishServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkAttendancePublishServiceApplication.class, args);
    }
}
