package com.gabrielluciano.payrollservice.infra.adapter;

import static java.math.BigDecimal.valueOf;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.gabrielluciano.payrollservice.domain.dto.InssTaxRate;
import com.gabrielluciano.payrollservice.domain.provider.InssTaxRateProvider;
import com.gabrielluciano.payrollservice.infra.exception.MicroserviceCommunicationErrorException;
import com.gabrielluciano.payrollservice.infra.httpclients.InssTaxServiceClient;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class InssTaxRateProviderAdapterTest {

    private final List<InssTaxRate> taxRates = List.of(
        new InssTaxRate(1L, valueOf(0.00),    valueOf(1412.00), valueOf(0.075)),
        new InssTaxRate(2L, valueOf(1412.01), valueOf(2666.68), valueOf(0.09)),
        new InssTaxRate(3L, valueOf(2666.69), valueOf(4000.03), valueOf(0.12)),
        new InssTaxRate(4L, valueOf(4000.04), valueOf(7786.02), valueOf(0.14))
    );

    @Mock
    private InssTaxServiceClient client;

    private InssTaxRateProvider inssTaxRateProviderAdapter;

    @BeforeEach
    void setUp() {
        inssTaxRateProviderAdapter = new InssTaxRateProviderAdapter(client);
    }

    @Test
    @DisplayName("getTaxRates return list of InssTaxRate when valid response")
    void getTaxRates_ShouldReturnListOfInssTaxRateWhenValidResponse() {
        var response = ResponseEntity.ok(taxRates);

        when(client.getTaxRates()).thenReturn(response);

        List<InssTaxRate> returnedRates = inssTaxRateProviderAdapter.getTaxRates();

        assertThat(returnedRates).hasSize(taxRates.size());
        assertThat(returnedRates.get(0).rate()).isEqualByComparingTo(taxRates.get(0).rate());
        assertThat(returnedRates.get(1).rate()).isEqualByComparingTo(taxRates.get(1).rate());
    }

    @Test
    @DisplayName("getTaxRates should throw MicroserviceCommunicationErrorException when return different from 2XX")
    void getTaxRates_ShouldThrowMicroserviceCommunicationErrorExceptionWhenReturnDifferentFrom2xx() {
        when(client.getTaxRates()).thenReturn(getErrorResponse(HttpStatus.NOT_FOUND));
        Assertions.assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> inssTaxRateProviderAdapter.getTaxRates());

        when(client.getTaxRates()).thenReturn(getErrorResponse(HttpStatus.BAD_REQUEST));
        Assertions.assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> inssTaxRateProviderAdapter.getTaxRates());

        when(client.getTaxRates()).thenReturn(getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> inssTaxRateProviderAdapter.getTaxRates());
    }

    private ResponseEntity<List<InssTaxRate>> getErrorResponse(HttpStatus status) {
        return ResponseEntity.status(status.value()).build();
    }

    @Test
    @DisplayName("getTaxRates should throws MicroserviceCommunicationErrorException when FeignException")
    void getTaxRates_ShouldThrowsMicroserviceCommunicationErrorExceptionWhenFeignException() {
        when(client.getTaxRates()).thenThrow(FeignException.FeignClientException.class);

        assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> inssTaxRateProviderAdapter.getTaxRates());
    }
}
