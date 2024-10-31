package com.gabrielluciano.payrollservice.infra.adapter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gabrielluciano.payrollservice.domain.model.IncomeTaxRate;
import com.gabrielluciano.payrollservice.domain.provider.IncomeTaxRateProvider;
import com.gabrielluciano.payrollservice.infra.exception.MicroserviceCommunicationErrorException;
import com.gabrielluciano.payrollservice.infra.httpclients.IncomeTaxServiceClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeTaxRateProviderAdapter implements IncomeTaxRateProvider {

    private final IncomeTaxServiceClient client;

    @Override
    public List<IncomeTaxRate> getTaxRates() {
        try {
            final var response = client.getTaxRates();
            if (!response.getStatusCode().is2xxSuccessful())
                throw new MicroserviceCommunicationErrorException(
                    "Error communicating with income-tax-service: " + response.getStatusCode());
            return response.getBody();
        } catch (FeignException.FeignClientException ex) {
            throw new MicroserviceCommunicationErrorException("Error communicating with income-tax-service", ex);
        }
    }
}
