package com.gabrielluciano.payrollservice.infra.adapter;

import java.util.List;

import org.springframework.stereotype.Service;

import com.gabrielluciano.payrollservice.domain.dto.IncomeTaxRate;
import com.gabrielluciano.payrollservice.domain.service.IncomeTaxService;
import com.gabrielluciano.payrollservice.infra.exception.MicroserviceCommunicationErrorException;
import com.gabrielluciano.payrollservice.infra.httpclients.IncomeTaxServiceClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IncomeTaxServiceAdapter implements IncomeTaxService {

    private final IncomeTaxServiceClient client;

    @Override
    public List<IncomeTaxRate> getTaxRates() {
        try {
            final var response = client.getTaxRates();
            if (!response.getStatusCode().is2xxSuccessful())
                throw new MicroserviceCommunicationErrorException(
                    "Error communicating with income-service: " + response.getStatusCode());
            return response.getBody();
        } catch (FeignException.FeignClientException ex) {
            throw new MicroserviceCommunicationErrorException("Error communicating with employee-service", ex);
        }
    }
}
