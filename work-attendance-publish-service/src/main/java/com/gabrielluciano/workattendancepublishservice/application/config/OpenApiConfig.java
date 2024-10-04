package com.gabrielluciano.workattendancepublishservice.application.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Work Attendance Publish Service API", description = "Work Attendance Publish Microservice REST API"))
public class OpenApiConfig {
}
