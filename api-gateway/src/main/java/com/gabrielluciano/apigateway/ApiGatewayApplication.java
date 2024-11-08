package com.gabrielluciano.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

    @Bean
    RouteLocator routes(
        RouteLocatorBuilder builder,
        @Value("${services.employeeService.url}") String employeeServiceUrl,
        @Value("${services.incomeTaxService.url}") String incomeTaxServiceUrl,
        @Value("${services.inssTaxService.url}") String inssTaxServiceUrl,
        @Value("${services.workAttendanceService.url}") String workAttendanceServiceUrl,
        @Value("${services.payrollService.url}") String payrollServiceUrl
    ) {
        return builder.routes()
                .route(r -> r.path("/positions/**", "/employees/**").uri(employeeServiceUrl))
                .route(r -> r.path("/income/**").uri(incomeTaxServiceUrl))
                .route(r -> r.path("/inss/**").uri(inssTaxServiceUrl))
                .route(r -> r.path("/work-attendances/**").uri(workAttendanceServiceUrl))
                .route(r -> r.path("/payrolls/**").uri(payrollServiceUrl))
                .build();
    }
}

