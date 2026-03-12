package com.techmarket.apigateway.infrastructure.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.UUID;

@Component
public class RequestLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_START_TIME = "requestStartTime";
    private static final String REQUEST_ID_HEADER = "X-Request-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // Generate or propagate request ID
        String requestId = request.getHeaders().getFirst(REQUEST_ID_HEADER);
        if (requestId == null || requestId.isBlank()) {
            requestId = UUID.randomUUID().toString();
        }

        // Mutate the request to carry the request ID downstream
        String finalRequestId = requestId;
        ServerHttpRequest mutatedRequest = request.mutate()
                .header(REQUEST_ID_HEADER, finalRequestId)
                .build();

        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(mutatedRequest)
                .build();

        long startTime = Instant.now().toEpochMilli();
        mutatedExchange.getAttributes().put(REQUEST_START_TIME, startTime);

        String method = request.getMethod().name();
        String path = request.getURI().getPath();
        String clientIp = request.getRemoteAddress() != null
                ? request.getRemoteAddress().getAddress().getHostAddress()
                : "unknown";

        log.info("Incoming request: requestId={}, method={}, path={}, clientIp={}",
                finalRequestId, method, path, clientIp);

        // Add request ID to response headers before response is committed
        mutatedExchange.getResponse().beforeCommit(() -> {
            mutatedExchange.getResponse().getHeaders().add(REQUEST_ID_HEADER, finalRequestId);
            return Mono.empty();
        });

        return chain.filter(mutatedExchange).then(Mono.fromRunnable(() -> {
            Long start = mutatedExchange.getAttribute(REQUEST_START_TIME);
            long duration = start != null ? Instant.now().toEpochMilli() - start : -1;
            ServerHttpResponse response = mutatedExchange.getResponse();
            int statusCode = response.getStatusCode() != null
                    ? response.getStatusCode().value()
                    : 0;

            log.info("Outgoing response: requestId={}, method={}, path={}, status={}, duration={}ms",
                    finalRequestId, method, path, statusCode, duration);
        }));
    }

    @Override
    public int getOrder() {
        // Execute early in the filter chain
        return Ordered.HIGHEST_PRECEDENCE + 1;
    }
}
