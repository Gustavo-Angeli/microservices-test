package com.gusta.apigateway.config;

import org.springframework.cloud.gateway.route.*;
import org.springframework.cloud.gateway.route.builder.*;
import org.springframework.context.annotation.*;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                .route(p -> p.path("/auth/**")
                        .uri("lb://auth-microservice/**"))
                .route(p -> p.path("/test/**")
                        .uri("lb://test-microservice"))
                .build();
    }

}
