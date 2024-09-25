package com.gabrielluciano.insstaxservice.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "INSS Tax Service API", description = "INSS Tax Microservice REST API"))
public class OpenApiConfig {
}
