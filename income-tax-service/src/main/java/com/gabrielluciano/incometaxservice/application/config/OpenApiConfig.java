package com.gabrielluciano.incometaxservice.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Income Tax Service API", description = "Income Tax Microservice REST API"))
public class OpenApiConfig {
}
