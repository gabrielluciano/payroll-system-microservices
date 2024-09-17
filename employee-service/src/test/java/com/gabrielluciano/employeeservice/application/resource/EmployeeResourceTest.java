package com.gabrielluciano.employeeservice.application.resource;

import com.gabrielluciano.employeeservice.config.TestcontainersConfiguration;
import com.gabrielluciano.employeeservice.domain.dto.CreateEmployeeRequest;
import com.gabrielluciano.employeeservice.domain.model.Position;
import com.gabrielluciano.employeeservice.infra.repository.EmployeeRepository;
import com.gabrielluciano.employeeservice.infra.repository.PositionRepository;
import com.gabrielluciano.employeeservice.util.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
class EmployeeResourceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        positionRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save employee")
    void shouldSaveEmployee() throws Exception {
        Position position = positionRepository.saveAndFlush(new Position(null, "Software Developer"));
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("John", "123.456.789-10",
                2000.01, position.getId());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(createEmployeeRequest.cpf())));
    }

    // TODO: Test Unhappy paths for save method - duplicated employee, race condition, non-existent position, invalid cpf, empty name and empty baseSalary
}
