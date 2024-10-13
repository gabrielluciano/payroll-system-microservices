package com.gabrielluciano.payrollservice.infra.httpclients;

import com.gabrielluciano.payrollservice.domain.dto.Employee;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${services.employeeServiceName}", path = "/employees")
public interface EmployeeServiceClient {

    @GetMapping("{cpf}")
    ResponseEntity<Employee> findByCpf(@PathVariable String cpf);
}

