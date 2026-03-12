package com.techmarket.apigateway.adapter.in.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    private static final Logger log = LoggerFactory.getLogger(FallbackController.class);

    @GetMapping("/{serviceName}")
    public Mono<Map<String, Object>> serviceFallback(
            @PathVariable String serviceName,
            ServerWebExchange exchange) {

        String requestId = exchange.getRequest().getHeaders().getFirst("X-Request-Id");
        String originalPath = exchange.getRequest().getURI().getPath();

        log.warn("Fallback triggered: service={}, originalPath={}, requestId={}",
                serviceName, originalPath, requestId);

        exchange.getResponse().setStatusCode(HttpStatus.SERVICE_UNAVAILABLE);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return Mono.just(Map.of(
                "status", HttpStatus.SERVICE_UNAVAILABLE.value(),
                "error", "Service Unavailable",
                "message", String.format("The %s is temporarily unavailable. Please try again later.",
                        formatServiceName(serviceName)),
                "service", serviceName,
                "timestamp", Instant.now().toString(),
                "requestId", requestId != null ? requestId : "unknown"
        ));
    }

    private String formatServiceName(String serviceName) {
        return serviceName.replace("-", " ");
    }
}
