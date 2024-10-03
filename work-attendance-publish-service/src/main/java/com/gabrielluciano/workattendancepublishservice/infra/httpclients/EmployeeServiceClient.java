package com.gabrielluciano.workattendancepublishservice.infra.httpclients;

import com.gabrielluciano.workattendancepublishservice.domain.dto.EmployeeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${services.employeeServiceName}", path = "/employees")
public interface EmployeeServiceClient {

    @GetMapping("{cpf}")
    ResponseEntity<EmployeeResponse> findByCpf(@PathVariable String cpf);
}
