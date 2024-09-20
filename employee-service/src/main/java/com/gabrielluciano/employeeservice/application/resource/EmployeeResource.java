package com.gabrielluciano.employeeservice.application.resource;

import com.gabrielluciano.employeeservice.application.resource.exception.StandardError;
import com.gabrielluciano.employeeservice.domain.dto.CreateEmployeeRequest;
import com.gabrielluciano.employeeservice.domain.dto.EmployeeResponse;
import com.gabrielluciano.employeeservice.domain.dto.UpdateEmployeeRequest;
import com.gabrielluciano.employeeservice.domain.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Employee", description = "Employee resource")
@RestController
@RequestMapping("employees")
@RequiredArgsConstructor
@Slf4j
public class EmployeeResource {

    private final EmployeeService service;

    @Operation(summary = "Get employee by cpf", description = "Returns the employee with the given cpf")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "404", description = "Employee not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @GetMapping("{cpf}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<EmployeeResponse> findByCpf(@PathVariable String cpf) {
        return ResponseEntity.ok(service.findByCpf(cpf));
    }

    @Operation(summary = "Save employee", description = "Creates a new employee")
    @ApiResponse(responseCode = "201", description = "Successfully created")
    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @ApiResponse(responseCode = "404", description = "Position not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @ApiResponse(responseCode = "409", description = "Duplicated data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Void> save(@RequestBody @Valid CreateEmployeeRequest body) {
        log.info("Received save employee request");
        EmployeeResponse employee = service.save(body);
        log.info("Successfully created employee with cpf '{}'", employee.cpf());
        return ResponseEntity.created(createLocation(employee)).build();
    }

    @Operation(summary = "Update employee", description = "Updates an existing employee")
    @ApiResponse(responseCode = "204", description = "Successfully updated")
    @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @ApiResponse(responseCode = "404", description = "Employee or position not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @PutMapping("{cpf}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> update(@PathVariable String cpf, @RequestBody @Valid UpdateEmployeeRequest body) {
        log.info("Received update employee request");
        EmployeeResponse employee = service.update(cpf, body);
        log.info("Successfully updated employee with cpf '{}'", employee.cpf());
        return ResponseEntity.noContent().build();
    }

    private URI createLocation(EmployeeResponse employeeResponse) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .pathSegment("{cpf}")
                .buildAndExpand(employeeResponse.cpf())
                .toUri();
    }
}
