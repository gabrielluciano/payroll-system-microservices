package com.gabrielluciano.payrollservice;

import com.gabrielluciano.payrollservice.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class PayrollServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
