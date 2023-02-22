package com.gusta.apigateway.filter;

import org.slf4j.*;
import org.springframework.cloud.gateway.filter.*;
import org.springframework.stereotype.*;
import org.springframework.web.server.*;
import reactor.core.publisher.*;

@Component
public class LogginFilter implements GlobalFilter {

    private Logger logger = LoggerFactory.getLogger(LogginFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Original request path -> {} ", exchange.getRequest().getPath());
        return chain.filter(exchange);
    }
}
