package com.gabrielluciano.employeeservice.application.resource;

import com.gabrielluciano.employeeservice.config.TestcontainersConfiguration;
import com.gabrielluciano.employeeservice.domain.dto.CreateEmployeeRequest;
import com.gabrielluciano.employeeservice.domain.dto.UpdateEmployeeRequest;
import com.gabrielluciano.employeeservice.domain.model.Employee;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
class EmployeeResourceTest {

    private static final String VALID_CPF = "127.361.540-96";
    private static final String INVALID_CPF = "123.456.789-10";

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
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("John", VALID_CPF,
                BigDecimal.valueOf(2000.01), position.getId());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString(createEmployeeRequest.cpf())));
    }

    @Test
    @DisplayName("Should not save empty name")
    void shouldNotSaveEmptyName() throws Exception {
        Position position = positionRepository.saveAndFlush(new Position(null, "Software Developer"));
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("", VALID_CPF,
                BigDecimal.valueOf(2000.01), position.getId());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/employees")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", containsString("name")));
    }

    @Test
    @DisplayName("Should not save empty baseSalary")
    void shouldNotSaveEmptyBaseSalary() throws Exception {
        Position position = positionRepository.saveAndFlush(new Position(null, "Software Developer"));
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("John", VALID_CPF,
                null, position.getId());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/employees")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", containsString("baseSalary")));
    }

    @Test
    @DisplayName("Should not save negative baseSalary")
    void shouldNotSaveNegativeBaseSalary() throws Exception {
        Position position = positionRepository.saveAndFlush(new Position(null, "Software Developer"));
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("John", VALID_CPF,
                BigDecimal.valueOf(-100.0), position.getId());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/employees")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", containsString("baseSalary")));
    }

    @Test
    @DisplayName("Should not save empty cpf")
    void shouldNotSaveEmptyCpf() throws Exception {
        Position position = positionRepository.saveAndFlush(new Position(null, "Software Developer"));
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("John", "",
                BigDecimal.valueOf(2000.01), position.getId());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/employees")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", containsString("cpf")));
    }

    @Test
    @DisplayName("Should not save invalid cpf")
    void shouldNotSaveInvalidCpf() throws Exception {
        Position position = positionRepository.saveAndFlush(new Position(null, "Software Developer"));
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("John", INVALID_CPF,
                BigDecimal.valueOf(2000.01), position.getId());

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/employees")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", containsString("cpf")));
    }

    @Test
    @DisplayName("Should not save when position does not exist")
    void shouldNotSaveWhenPositionDoesNotExist() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("John", VALID_CPF,
                BigDecimal.valueOf(2000.01), 1L);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path", equalTo("/employees")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.error", equalTo("Entity Not Found")))
                .andExpect(jsonPath("$.message", containsString("Position")));
    }

    @Test
    @DisplayName("Should not save duplicated employee")
    void shouldNotSaveDuplicatedEmployee() throws Exception {
        Position position = positionRepository.saveAndFlush(new Position(null, "Software Developer"));
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("John", VALID_CPF,
                BigDecimal.valueOf(2000.01), position.getId());
        Employee employee = createEmployeeRequest.toModel();
        employee.setPosition(position);
        employeeRepository.saveAndFlush(employee);

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(createEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.path", equalTo("/employees")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.CONFLICT.value())))
                .andExpect(jsonPath("$.error", equalTo("Duplicated Entity")))
                .andExpect(jsonPath("$.message", containsString(createEmployeeRequest.cpf())));
    }

    @Test
    @DisplayName("Should find employee by cpf")
    void shouldFindEmployeeByCpf() throws Exception {
        Position position = positionRepository.saveAndFlush(new Position(null, "Software Developer"));
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest("John", VALID_CPF,
                BigDecimal.valueOf(2000.01), position.getId());
        Employee employee = createEmployeeRequest.toModel();
        employee.setPosition(position);
        employeeRepository.saveAndFlush(employee);

        mockMvc.perform(get("/employees/" + employee.getCpf()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(employee.getName())))
                .andExpect(jsonPath("$.cpf", equalTo(employee.getCpf())))
                .andExpect(jsonPath("$.position.name", equalTo(employee.getPosition().getName())));
    }

    @Test
    @DisplayName("Should not find employee by cpf")
    void shouldNotFindEmployeeByCpf() throws Exception {
        mockMvc.perform(get("/employees/" + VALID_CPF))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path", equalTo("/employees/" + VALID_CPF)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.error", equalTo("Entity Not Found")))
                .andExpect(jsonPath("$.message", containsString("Employee")));
    }

    @Test
    @DisplayName("Should update employee")
    void shouldUpdateEmployee() throws Exception {
        Position position1 = positionRepository.saveAndFlush(new Position(null, "Software Developer"));
        Position position2 = positionRepository.saveAndFlush(new Position(null, "Quality Assurance Analyst"));
        Employee employee = employeeRepository.saveAndFlush(new Employee(VALID_CPF, "John", BigDecimal.valueOf(2000.01), position1));

        var updateEmployeeRequest = new UpdateEmployeeRequest("Jack", BigDecimal.valueOf(5000.01), position2.getId());

        mockMvc.perform(put("/employees/" + employee.getCpf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(updateEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isNoContent());

        Employee updatedEmployee = employeeRepository.findByCpf(employee.getCpf()).get();

        assertThat(updatedEmployee.getName()).isEqualTo(updateEmployeeRequest.name());
        assertThat(updatedEmployee.getBaseSalary()).isEqualTo(updateEmployeeRequest.baseSalary());
        assertThat(updatedEmployee.getPosition().getId()).isEqualTo(updateEmployeeRequest.positionId());
    }

    @Test
    @DisplayName("Should return not found when updating employee that does not exist")
    void shouldReturnNotFoundWhenUpdatingEmployeeThatDoesNotExist() throws Exception {
        Position position = positionRepository.saveAndFlush(new Position(null, "Software Developer"));

        var updateEmployeeRequest = new UpdateEmployeeRequest("Jack", BigDecimal.valueOf(5000.01), position.getId());

        mockMvc.perform(put("/employees/" + VALID_CPF)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(updateEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path", equalTo("/employees/" + VALID_CPF)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.error", equalTo("Entity Not Found")))
                .andExpect(jsonPath("$.message", containsString("Employee")));
    }

    @Test
    @DisplayName("Should return not found when updating employee and position does not exist")
    void shouldReturnNotFoundWhenUpdatingEmployeeAndPositionDoesNotExist() throws Exception {
        Position position = positionRepository.saveAndFlush(new Position(null, "Software Developer"));
        employeeRepository.saveAndFlush(new Employee(VALID_CPF, "John", BigDecimal.valueOf(2000.01), position));

        var updateEmployeeRequest = new UpdateEmployeeRequest("Jack", BigDecimal.valueOf(5000.01), position.getId() + 1);

        mockMvc.perform(put("/employees/" + VALID_CPF)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(updateEmployeeRequest)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path", equalTo("/employees/" + VALID_CPF)))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.error", equalTo("Entity Not Found")))
                .andExpect(jsonPath("$.message", containsString("Position")));
    }
}
