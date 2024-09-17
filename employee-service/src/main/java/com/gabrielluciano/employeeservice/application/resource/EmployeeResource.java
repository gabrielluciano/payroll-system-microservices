package com.gabrielluciano.employeeservice.application.resource;

import com.gabrielluciano.employeeservice.domain.dto.CreateEmployeeRequest;
import com.gabrielluciano.employeeservice.domain.dto.EmployeeResponse;
import com.gabrielluciano.employeeservice.domain.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeResource {

    private final EmployeeService service;

    @GetMapping("{cpf}")
    public ResponseEntity<EmployeeResponse> findByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.findByCpf(cpf));
    }

    @PostMapping
    public ResponseEntity<Void> save(@RequestBody @Valid CreateEmployeeRequest body) {
        log.info("Received save employee request");
        EmployeeResponse employee = service.save(body);
        log.info("Successfully created employee with cpf '{}'", employee.cpf());
        return ResponseEntity.created(createLocation(employee)).build();
    }

    private URI createLocation(EmployeeResponse employeeResponse) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment("{cpf}")
                .buildAndExpand(employeeResponse.cpf())
                .toUri();
    }
}
