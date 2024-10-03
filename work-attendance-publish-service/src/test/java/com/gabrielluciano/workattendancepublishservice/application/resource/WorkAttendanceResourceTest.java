package com.gabrielluciano.workattendancepublishservice.application.resource;

import com.gabrielluciano.workattendancepublishservice.domain.dto.CreateWorkAttendanceRequest;
import com.gabrielluciano.workattendancepublishservice.domain.model.WorkAttendanceRecord;
import com.gabrielluciano.workattendancepublishservice.domain.service.EmployeeService;
import com.gabrielluciano.workattendancepublishservice.infra.exception.MicroserviceCommunicationErrorException;
import com.gabrielluciano.workattendancepublishservice.util.JsonUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"eureka.client.enabled=false"})
@AutoConfigureMockMvc
class WorkAttendanceResourceTest {

    private static final String VALID_CPF = "127.361.540-96";
    private static final String INVALID_CPF = "123.456.789-10";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KafkaTemplate<String, WorkAttendanceRecord> kafkaTemplate;

    @MockBean
    private EmployeeService employeeService;

    @Test
    @DisplayName("Should publish work attendance record to kafka")
    void shouldPublishSaveWorkAttendanceRecordToKafka() throws Exception {
        when(kafkaTemplate.sendDefault(ArgumentMatchers.anyString(), ArgumentMatchers.any()))
                .thenReturn(CompletableFuture.completedFuture(null));
        when(employeeService.existsByCpf(ArgumentMatchers.anyString()))
                .thenReturn(true);

        var request = new CreateWorkAttendanceRequest(VALID_CPF, 2024, 10, 180, 180);

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(kafkaTemplate, times(1)).sendDefault(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should not publish record when cpf does not exist")
    void shouldNotPublishRecordWhenCpfDoesNotExist() throws Exception {
        var request = new CreateWorkAttendanceRequest(VALID_CPF, 2024, 10, 180, 180);
        when(employeeService.existsByCpf(ArgumentMatchers.anyString()))
                .thenReturn(false);

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.path", equalTo("/work-attendances")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.error", equalTo("Entity Not Found")))
                .andExpect(jsonPath("$.message", containsString("Employee")));

        verify(kafkaTemplate, never()).sendDefault(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should return 500 when error communicating with employee service")
    void shouldWhenErrorCommunicatingWithEmployeeService() throws Exception {
        var request = new CreateWorkAttendanceRequest(VALID_CPF, 2024, 10, 180, 180);
        when(employeeService.existsByCpf(ArgumentMatchers.anyString()))
                .thenThrow(MicroserviceCommunicationErrorException.class);

        mockMvc.perform(post("/work-attendances")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtils.asJsonString(request)))
                .andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.path", equalTo("/work-attendances")))
                .andExpect(jsonPath("$.status", equalTo(HttpStatus.INTERNAL_SERVER_ERROR.value())))
                .andExpect(jsonPath("$.error", equalTo("Service Communication Error")));

        verify(kafkaTemplate, never()).sendDefault(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should not publish record with null attributes")
    void shouldNotPublishRecordWithNullAttributes() throws Exception {
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

        verify(kafkaTemplate, never()).sendDefault(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should not publish record invalid employeeCpf")
    void shouldNotPublishRecordInvalidEmployeeCpf() throws Exception {
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

        verify(kafkaTemplate, never()).sendDefault(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should not publish record with year less than 1970")
    void shouldNotPublishRecordWithYearLessThan1970() throws Exception {
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

        verify(kafkaTemplate, never()).sendDefault(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should not publish record with month not between 1 and 12")
    void shouldNotPublishRecordWithMonthNotBetween1And12() throws Exception {
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

        verify(kafkaTemplate, never()).sendDefault(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }

    @Test
    @DisplayName("Should not publish record with negative expectedWorkingHours and actualWorkingHours")
    void shouldNotPublishRecordWithNegativeExpectedWorkingHoursAndActualWorkingHours() throws Exception {
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

        verify(kafkaTemplate, never()).sendDefault(ArgumentMatchers.anyString(), ArgumentMatchers.any());
    }
}
