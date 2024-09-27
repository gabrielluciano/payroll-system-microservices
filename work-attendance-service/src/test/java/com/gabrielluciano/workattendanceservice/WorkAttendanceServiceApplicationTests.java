package com.gabrielluciano.workattendanceservice;

import com.gabrielluciano.workattendanceservice.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class WorkAttendanceServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
