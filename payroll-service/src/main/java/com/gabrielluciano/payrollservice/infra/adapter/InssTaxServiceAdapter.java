package com.gabrielluciano.payrollservice.infra.adapter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gabrielluciano.payrollservice.domain.dto.InssTaxRate;
import com.gabrielluciano.payrollservice.domain.service.InssTaxService;
import com.gabrielluciano.payrollservice.infra.exception.MicroserviceCommunicationErrorException;
import com.gabrielluciano.payrollservice.infra.httpclients.InssTaxServiceClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InssTaxServiceAdapter implements InssTaxService {

    private final InssTaxServiceClient client;

    @Override
    public List<InssTaxRate> getTaxRates() {
        try {
            final var response = client.getTaxRates();
            if (!response.getStatusCode().is2xxSuccessful())
                throw new MicroserviceCommunicationErrorException(
                    "Error communicating with inss-tax-service: " + response.getStatusCode());
            return response.getBody();
        } catch (FeignException.FeignClientException ex) {
            throw new MicroserviceCommunicationErrorException("Error communicating with inss-tax-service", ex);
        }
    }
}
