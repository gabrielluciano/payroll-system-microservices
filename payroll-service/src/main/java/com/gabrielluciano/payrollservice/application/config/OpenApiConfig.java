package com.gabrielluciano.payrollservice.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Payroll Service API", description = "Payroll Microservice REST API"))
public class OpenApiConfig {
}

