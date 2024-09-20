package com.gabrielluciano.employeeservice.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Employee Service API", description = "Employee Microservice REST API"))
public class OpenApiConfig {
}
