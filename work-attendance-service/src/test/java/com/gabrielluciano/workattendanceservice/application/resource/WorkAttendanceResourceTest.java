package com.gabrielluciano.workattendanceservice.application.resource;

import com.gabrielluciano.workattendanceservice.config.TestcontainersConfiguration;
import com.gabrielluciano.workattendanceservice.domain.dto.CreateWorkAttendanceRequest;
import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.workattendanceservice.domain.model.WorkAttendanceRecordId;
import com.gabrielluciano.workattendanceservice.infra.repository.WorkAttendanceRecordRepository;
import com.gabrielluciano.workattendanceservice.util.JsonUtils;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
@AutoConfigureMockMvc
class WorkAttendanceResourceTest {

    private static final String VALID_CPF = "127.361.540-96";
    private static final String INVALID_CPF = "123.456.789-10";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WorkAttendanceRecordRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Should save work attendance record")
    void shouldSaveWorkAttendanceRecord() throws Exception {
        int year = 2024;
        int month = 10;
        String expectedLocation = String.format("/work-attendances/%s/%d/%d", VALID_CPF, year, month);
        var request = new CreateWorkAttendanceRequest(VALID_CPF, year, month, 180, 180);

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("location", containsString(expectedLocation)));

        assertThat(repository.count()).isOne();
    }

    @Test
    @DisplayName("Should not save work attendance record with same employeeCpf month and year")
    void shouldNotSaveWorkAttendanceRecordWithSameEmployeeCpfMonthAndYear() throws Exception {
        int year = 2024;
        int month = 10;
        var id = new WorkAttendanceRecordId(VALID_CPF, year, month);
        repository.saveAndFlush(new WorkAttendanceRecord(id, 180, 180));

        var request = new CreateWorkAttendanceRequest(VALID_CPF, year, month, 180, 180);

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.path", equalTo("/work-attendances")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.CONFLICT.value())))
                .andExpect(jsonPath("$.error", equalTo("Duplicated Entity")))
                .andExpect(jsonPath("$.message", allOf(
                        containsString(Integer.toString(year)),
                        containsString(Integer.toString(month)),
                        containsString(VALID_CPF)
                )));

        assertThat(repository.count()).isOne();
    }


    @Test
    @DisplayName("Should not save null attributes")
    void shouldNotSaveNullAttributes() throws Exception {
        var request = new CreateWorkAttendanceRequest(null, null, null, null, null);

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/work-attendances")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", allOf(
                        containsString("employeeCpf"),
                        containsString("year"),
                        containsString("month"),
                        containsString("expectedWorkingHours"),
                        containsString("actualWorkingHours")
                )));

        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should not save invalid employeeCpf")
    void shouldNotSaveInvalidEmployeeCpf() throws Exception {
        var request = new CreateWorkAttendanceRequest(INVALID_CPF, 2024, 10, 180, 180);

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/work-attendances")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", containsString("employeeCpf")));

        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should not save with year less than 1970")
    void shouldNotSaveWithYearLessThan1970() throws Exception {
        var request = new CreateWorkAttendanceRequest(VALID_CPF, 1000, 10, 180, 180);

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/work-attendances")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", containsString("year")));

        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should not save with month not between 1 and 12")
    void shouldNotSaveWithMonthNotBetween1And12() throws Exception {
        var request1 = new CreateWorkAttendanceRequest(VALID_CPF, 2000, 0, 180, 180);
        var request2 = new CreateWorkAttendanceRequest(VALID_CPF, 2000, 13, 180, 180);

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request1)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/work-attendances")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", containsString("month")));

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request2)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/work-attendances")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", containsString("month")));

        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should not save with negative expectedWorkingHours and actualWorkingHours")
    void shouldNotSaveWithNegativeExpectedWorkingHoursAndActualWorkingHours() throws Exception {
        var request = new CreateWorkAttendanceRequest(VALID_CPF, 1000, 11, -10, -10);

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.path", equalTo("/work-attendances")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.BAD_REQUEST.value())))
                .andExpect(jsonPath("$.error", equalTo("Constraint Violation")))
                .andExpect(jsonPath("$.message", allOf(
                        containsString("expectedWorkingHours"),
                        containsString("actualWorkingHours"))
                ));

        assertThat(repository.count()).isZero();
    }

    @Test
    @DisplayName("Should find by employeeCpf, year and month")
    void shouldFindByEmployeeCpfYearAndMonth() throws Exception {
        int year = 2024;
        int month = 10;
        var id = new WorkAttendanceRecordId(VALID_CPF, 2024, 10);
        var record = repository.saveAndFlush(new WorkAttendanceRecord(id, 180, 180));

        mockMvc.perform(get("/work-attendances/{cpf}/{year}/{month}", VALID_CPF, year, month))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.employeeCpf", equalTo(record.getRecordId().getEmployeeCpf())))
                .andExpect(jsonPath("$.year", equalTo(record.getRecordId().getYear())))
                .andExpect(jsonPath("$.month", equalTo(record.getRecordId().getMonth())))
                .andExpect(jsonPath("$.expectedWorkingHours", equalTo(record.getExpectedWorkingHours())))
                .andExpect(jsonPath("$.actualWorkingHours", equalTo(record.getActualWorkingHours())));
    }

    @Test
    @DisplayName("Should not find by employeeCpf, year and month and return not found")
    void shouldNotFindByEmployeeCpfYearAndMonthAndReturnNotFound() throws Exception {
        int year = 2024;
        int month = 10;

        mockMvc.perform(get("/work-attendances/{cpf}/{year}/{month}", VALID_CPF, year, month))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path", equalTo(String.format("/work-attendances/%s/%d/%d", VALID_CPF, year, month))))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.error", equalTo("Entity Not Found")))
                .andExpect(jsonPath("$.message", containsString("WorkAttendanceRecord")));

    }
}
