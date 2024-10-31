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

import com.gabrielluciano.payrollservice.domain.dto.IncomeTaxRate;
import com.gabrielluciano.payrollservice.domain.provider.IncomeTaxRateProvider;
import com.gabrielluciano.payrollservice.infra.exception.MicroserviceCommunicationErrorException;
import com.gabrielluciano.payrollservice.infra.httpclients.IncomeTaxServiceClient;

import feign.FeignException;

@ExtendWith(MockitoExtension.class)
class IncomeTaxRateProviderAdapterTest {

    private final List<IncomeTaxRate> taxRates = List.of(
        new IncomeTaxRate(null, valueOf(0.00),    valueOf(2112.00), valueOf(0.00), valueOf(0.00)),
        new IncomeTaxRate(null, valueOf(2112.01), valueOf(2826.65), valueOf(0.075), valueOf(158.40)),
        new IncomeTaxRate(null, valueOf(2826.66), valueOf(3751.05), valueOf(0.15), valueOf(370.40)),
        new IncomeTaxRate(null, valueOf(3751.06), valueOf(4664.68), valueOf(0.225), valueOf(651.73)),
        new IncomeTaxRate(null, valueOf(4664.69), valueOf(10000000.00), valueOf(0.275), valueOf(884.96))
    );

    @Mock
    private IncomeTaxServiceClient client;

    private IncomeTaxRateProvider incomeTaxRateProviderAdapter;

    @BeforeEach
    void setUp() {
        incomeTaxRateProviderAdapter = new IncomeTaxRateProviderAdapter(client);
    }

    @Test
    @DisplayName("getTaxRates return list of IncomeTaxRate when valid response")
    void getTaxRates_ShouldReturnListOfIncomeTaxRateWhenValidResponse() {
        var response = ResponseEntity.ok(taxRates);

        when(client.getTaxRates()).thenReturn(response);

        List<IncomeTaxRate> returnedRates = incomeTaxRateProviderAdapter.getTaxRates();

        assertThat(returnedRates).hasSize(taxRates.size());
        assertThat(returnedRates.get(0).rate()).isEqualByComparingTo(taxRates.get(0).rate());
    }

    @Test
    @DisplayName("getTaxRates should throw MicroserviceCommunicationErrorException when return different from 2XX")
    void getTaxRates_ShouldThrowMicroserviceCommunicationErrorExceptionWhenReturnDifferentFrom2xx() {
        when(client.getTaxRates()).thenReturn(getErrorResponse(HttpStatus.NOT_FOUND));
        Assertions.assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> incomeTaxRateProviderAdapter.getTaxRates());

        when(client.getTaxRates()).thenReturn(getErrorResponse(HttpStatus.BAD_REQUEST));
        Assertions.assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> incomeTaxRateProviderAdapter.getTaxRates());

        when(client.getTaxRates()).thenReturn(getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR));
        Assertions.assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> incomeTaxRateProviderAdapter.getTaxRates());
    }

    private ResponseEntity<List<IncomeTaxRate>> getErrorResponse(HttpStatus status) {
        return ResponseEntity.status(status.value()).build();
    }

    @Test
    @DisplayName("getTaxRates should throws MicroserviceCommunicationErrorException when FeignException")
    void getTaxRates_ShouldThrowsMicroserviceCommunicationErrorExceptionWhenFeignException() {
        when(client.getTaxRates()).thenThrow(FeignException.FeignClientException.class);

        assertThatExceptionOfType(MicroserviceCommunicationErrorException.class)
            .isThrownBy(() -> incomeTaxRateProviderAdapter.getTaxRates());
    }
}
