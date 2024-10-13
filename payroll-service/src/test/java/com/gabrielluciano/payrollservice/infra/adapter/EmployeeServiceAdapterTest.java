package com.gabrielluciano.payrollservice.infra.adapter;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gabrielluciano.payrollservice.domain.dto.Employee;
import com.gabrielluciano.payrollservice.domain.dto.EmployeePosition;
import com.gabrielluciano.payrollservice.domain.service.EmployeeService;
import com.gabrielluciano.payrollservice.domain.service.exception.EntityNotFoundException;
import com.gabrielluciano.payrollservice.infra.exception.MicroserviceCommunicationErrorException;
import com.gabrielluciano.payrollservice.infra.httpclients.EmployeeServiceClient;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceAdapterTest {

    private static final String VALID_CPF = "127.361.540-96";

    @Mock
    private EmployeeServiceClient client;

    private EmployeeService employeeServiceAdapter;

    @BeforeEach
    void setUp() {
        employeeServiceAdapter = new EmployeeServiceAdapter(client);
    }

    @Test
    @DisplayName("findByCpf should return Employee when valid response")
    void findByCpf_ShouldReturnEmployeeWhenValidResponse() {
        Employee expected = new Employee("Tester", VALID_CPF, valueOf(1000.00), new EmployeePosition(1L, "Software Dev"));
        var response = ResponseEntity.ok(expected);

        when(client.findByCpf(anyString())).thenReturn(response);

        Employee employee = employeeServiceAdapter.findByCpf(VALID_CPF);

        assertThat(employee.name()).isEqualTo(expected.name());
        assertThat(employee.cpf()).isEqualTo(expected.cpf());
        assertThat(employee.baseSalary()).isEqualTo(expected.baseSalary());
    }

    @Test
    @DisplayName("findByCpf should throw EntityNotFoundException when employee not found")
    void findByCpf_ShouldThrowEntityNotFoundExceptionWhenEmployeeNotFound() {
        when(client.findByCpf(anyString())).thenThrow(new EntityNotFoundException(VALID_CPF, Employee.class));

        Assertions.assertThatExceptionOfType(EntityNotFoundException.class)
            .isThrownBy(() -> employeeServiceAdapter.findByCpf(VALID_CPF));
    }

    @Test
    @DisplayName("findByCpf should throw MicroserviceCommunicationErrorException when return different from 2XX")
    void findByCpf_ShouldThrowMicroserviceCommunicationErrorExceptionWhenReturnDifferentFrom2xx() {
        when(client.findByCpf(anyString())).thenReturn(getErrorResponse(HttpStatus.NOT_FOUND));
        Assertions.assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> employeeServiceAdapter.findByCpf(VALID_CPF));

        when(client.findByCpf(anyString())).thenReturn(getErrorResponse(HttpStatus.BAD_REQUEST));
        Assertions.assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> employeeServiceAdapter.findByCpf(VALID_CPF));

        when(client.findByCpf(anyString())).thenReturn(getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> employeeServiceAdapter.findByCpf(VALID_CPF));
    }

    private ResponseEntity<Employee> getErrorResponse(HttpStatus status) {
        return ResponseEntity.status(status.value()).build();
    }

    @Test
    @DisplayName("findByCpf should throws MicroserviceCommunicationErrorException when FeignException")
    void findByCpf_ShouldThrowsMicroserviceCommunicationErrorExceptionWhenFeignException() {
        when(client.findByCpf(anyString())).thenThrow(FeignException.FeignClientException.class);

        assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> employeeServiceAdapter.findByCpf(VALID_CPF));
    }
}
