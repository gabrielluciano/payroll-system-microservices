package com.gabrielluciano.payrollservice.application.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielluciano.payrollservice.application.resource.exception.StandardError;
import com.gabrielluciano.payrollservice.domain.dto.PayrollResponse;
import com.gabrielluciano.payrollservice.domain.service.PayrollService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "Payroll", description = "Payroll resource")
@RestController
@RequestMapping("payrolls")
@RequiredArgsConstructor
public class PayrollResource {

    private final PayrollService service;

    @Operation(summary = "Get payroll by cpf, year and month", description = "Returns the payroll for the given cpf, year and month")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved")
    @ApiResponse(responseCode = "404", description = "Payroll not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardError.class)))
    @GetMapping("{cpf}/{year}/{month}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<PayrollResponse> getPayroll(@PathVariable String cpf, @PathVariable Integer year, @PathVariable Integer month) {
        return ResponseEntity.ok(service.getPayroll(cpf, year, month));
    }
}
