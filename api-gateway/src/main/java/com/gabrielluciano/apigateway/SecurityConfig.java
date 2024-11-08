package com.gabrielluciano.apigateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CorsSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MapReactiveUserDetailsService userDetailsService(
        @Value("${security.username}") String username,
        @Value("${security.password}") String password 
    ) {
        UserDetails service = User.withUsername(username)
                .password(passwordEncoder().encode(password))
                .build();
        return new MapReactiveUserDetailsService(service);
    }

    @Bean
    @Order(1)
    public SecurityWebFilterChain metricsSecurityWebFilterChain(ServerHttpSecurity http) {
        http
            .securityMatcher(ServerWebExchangeMatchers.pathMatchers("/actuator/**"))
            .authorizeExchange(exchanges -> exchanges
                .anyExchange().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .csrf(CsrfSpec::disable)
            .cors(CorsSpec::disable);
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityWebFilterChain oAuth2SecurityWebFilterChain(ServerHttpSecurity http) {
        http
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/employees/swagger-ui/**", "/employees/api-docs/**").permitAll()
                .pathMatchers("/inss/swagger-ui/**", "/inss/api-docs/**").permitAll()
                .pathMatchers("/income/swagger-ui/**", "/income/api-docs/**").permitAll()
                .pathMatchers("/work-attendances/swagger-ui/**", "/work-attendances/api-docs/**").permitAll()
                .pathMatchers("/payrolls/swagger-ui/**", "/payrolls/api-docs/**").permitAll()
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults())
            );
        return http.build();
    }
}
