package com.gabrielluciano.payrollservice.infra.adapter;

import org.springframework.stereotype.Service;

import com.gabrielluciano.payrollservice.domain.dto.Employee;
import com.gabrielluciano.payrollservice.domain.service.EmployeeService;
import com.gabrielluciano.payrollservice.domain.service.exception.EntityNotFoundException;
import com.gabrielluciano.payrollservice.infra.exception.MicroserviceCommunicationErrorException;
import com.gabrielluciano.payrollservice.infra.httpclients.EmployeeServiceClient;

import feign.FeignException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceAdapter implements EmployeeService {

    private final EmployeeServiceClient client;

    @Override
    public Employee findByCpf(String cpf) throws EntityNotFoundException {
        try {
            final var response = client.findByCpf(cpf);
            if (response.getStatusCode().is2xxSuccessful())
                return response.getBody();
            throw new MicroserviceCommunicationErrorException("Error communicating with employee-service: " + response.getStatusCode());
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException(cpf, Employee.class);
        } catch (FeignException.FeignClientException ex) {
            throw new MicroserviceCommunicationErrorException("Error communicating with employee-service", ex);
        }
    }
}
