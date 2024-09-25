package com.gabrielluciano.incometaxservice;

import com.gabrielluciano.incometaxservice.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class IncomeTaxServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
