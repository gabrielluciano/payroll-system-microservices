package com.gabrielluciano.employeeservice;

import com.gabrielluciano.employeeservice.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class EmployeeServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
