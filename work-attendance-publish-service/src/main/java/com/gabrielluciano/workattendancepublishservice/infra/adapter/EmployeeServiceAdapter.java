package com.gabrielluciano.workattendancepublishservice.infra.adapter;

import com.gabrielluciano.workattendancepublishservice.domain.service.EmployeeService;
import com.gabrielluciano.workattendancepublishservice.infra.exception.MicroserviceCommunicationErrorException;
import com.gabrielluciano.workattendancepublishservice.infra.httpclients.EmployeeServiceClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeServiceAdapter implements EmployeeService {

    public final EmployeeServiceClient client;

    @Override
    public boolean existsByCpf(String cpf) {
        try {
            final var response = client.findByCpf(cpf);
            if (response.getStatusCode().isError())
                return false;
            final var body = response.getBody();
            return body != null && !body.cpf().isBlank();
        } catch (FeignException.NotFound ex) {
            return false;
        } catch (FeignException.FeignClientException ex) {
            throw new MicroserviceCommunicationErrorException("Error communicating with employee-service", ex);
        }
    }
}
