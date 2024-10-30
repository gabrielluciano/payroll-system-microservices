package com.gabrielluciano.workattendancepublishservice.infra.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record EmployeeResponse(String name, String cpf, BigDecimal baseSalary,
                               @JsonProperty("position") PositionResponse positionResponse) {

}
