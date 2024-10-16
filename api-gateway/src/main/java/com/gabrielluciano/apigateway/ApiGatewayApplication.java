package com.gabrielluciano.apigateway;

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
	RouteLocator routes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(r -> r.path("/positions/**", "/employees/**").uri("lb://employee-service"))
				.route(r -> r.path("/income/**").uri("lb://income-tax-service"))
				.route(r -> r.path("/inss/**").uri("lb://inss-tax-service"))
				.route(r -> r.path("/work-attendances/**").uri("lb://work-attendance-service"))
				.build();
	}

}
