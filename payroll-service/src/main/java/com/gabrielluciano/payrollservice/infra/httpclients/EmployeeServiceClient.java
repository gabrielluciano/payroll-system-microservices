package com.gabrielluciano.payrollservice.infra.httpclients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gabrielluciano.payrollservice.domain.dto.EmployeeResponse;

@FeignClient(name = "${services.employeeService.name}", url = "${services.employeeService.url}", path = "/employees")
public interface EmployeeServiceClient {

    @GetMapping("{cpf}")
    ResponseEntity<EmployeeResponse> findByCpf(@PathVariable String cpf);
}

