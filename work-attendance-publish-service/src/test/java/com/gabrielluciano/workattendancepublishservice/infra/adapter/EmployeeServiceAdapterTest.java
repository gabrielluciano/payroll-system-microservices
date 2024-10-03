package com.gabrielluciano.workattendancepublishservice.infra.adapter;

import com.gabrielluciano.workattendancepublishservice.domain.dto.EmployeeResponse;
import com.gabrielluciano.workattendancepublishservice.domain.dto.PositionResponse;
import com.gabrielluciano.workattendancepublishservice.domain.service.EmployeeService;
import com.gabrielluciano.workattendancepublishservice.infra.exception.MicroserviceCommunicationErrorException;
import com.gabrielluciano.workattendancepublishservice.infra.httpclients.EmployeeServiceClient;
import feign.FeignException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
    @DisplayName("existsByCpf should return true when valid response")
    void existsByCpf_ShouldReturnTrueWhenValidResponse() {
        var response = ResponseEntity.ok(new EmployeeResponse("Tester", VALID_CPF, valueOf(1000.00),
                new PositionResponse(1L, "Software Dev")));
        when(client.findByCpf(anyString())).thenReturn(response);

        assertThat(employeeServiceAdapter.existsByCpf(VALID_CPF)).isTrue();
    }

    @Test
    @DisplayName("existsByCpf should return false when error response")
    void existsByCpf_ShouldReturnFalseWhenErrorResponse() {
        when(client.findByCpf(anyString())).thenReturn(getErrorResponse(HttpStatus.BAD_REQUEST));
        assertThat(employeeServiceAdapter.existsByCpf(VALID_CPF)).isFalse();

        when(client.findByCpf(anyString())).thenReturn(getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR));
        assertThat(employeeServiceAdapter.existsByCpf(VALID_CPF)).isFalse();

        when(client.findByCpf(anyString())).thenReturn(getErrorResponse(HttpStatus.NOT_FOUND));
        assertThat(employeeServiceAdapter.existsByCpf(VALID_CPF)).isFalse();
    }

    private ResponseEntity<EmployeeResponse> getErrorResponse(HttpStatus status) {
        return ResponseEntity.status(status.value()).build();
    }

    @Test
    @DisplayName("existsByCpf should return false when Feign NotFound exception")
    void existsByCpf_ShouldReturnFalseWhenFeignNotFoundException() {
        when(client.findByCpf(anyString())).thenThrow(FeignException.NotFound.class);
        assertThat(employeeServiceAdapter.existsByCpf(VALID_CPF)).isFalse();
    }

    @Test
    @DisplayName("existsByCpf should throws MicroserviceCommunicationErrorException when FeignException")
    void existsByCpf_ShouldThrowsMicroserviceCommunicationErrorExceptionWhenFeignException() {
        when(client.findByCpf(anyString())).thenThrow(FeignException.FeignClientException.class);

        assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
                .isThrownBy(() -> employeeServiceAdapter.existsByCpf(VALID_CPF));
    }
}
