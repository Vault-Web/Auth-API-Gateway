package vaultweb.apigateway.config;

//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//import vaultweb.apigateway.exceptions.DefaultException;
//import vaultweb.apigateway.exceptions.dto.DefaultExceptionLevels;
//import vaultweb.apigateway.util.JwtUtil;
//
//import java.util.List;

//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter implements GlobalFilter, Ordered {
//
//    private final JwtUtil jwtUtil;
//    @Value("${auth.publicUrls}")
//    public List<String> publicEndpoints;
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String path = request.getURI().getPath();
//
//        // Skip authentication for public endpoints
//        if (isPublicPath(path)) {
//            return chain.filter(exchange);
//        }
//
//        // Extract token from Authorization header
//        String authHeader = request.getHeaders().getFirst("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            return Mono.error(new DefaultException("Missing or invalid Authorization header", DefaultExceptionLevels.AUTHENTICATION_EXCEPTION));
//        }
//
//        String token = authHeader.substring(7);
//
//        // Validate token
//        if (!jwtUtil.validateToken(token)) {
//            return Mono.error(new DefaultException("Invalid or expired token", DefaultExceptionLevels.AUTHENTICATION_EXCEPTION));
//        }
//
//        // Extract username and add to request headers for downstream services
//        String username = jwtUtil.extractSubject(token);
//        ServerHttpRequest modifiedRequest = request.mutate()
//                .header("X-Auth-Username", username)
//                .build();
//
//        return chain.filter(exchange.mutate().request(modifiedRequest).build());
//    }
//
//    private boolean isPublicPath(String path) {
//        return publicEndpoints.stream().anyMatch(path::startsWith);
//    }
//
//    @Override
//    public int getOrder() {
//        return -100; // Execute before other filters
//    }
//}


import lombok.NonNull;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import vaultweb.apigateway.util.JwtUtil;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    @NonNull
    public Mono<Void> filter(ServerWebExchange exchange, @NonNull WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return sendErrorResponse(exchange, "Missing or invalid Authorization header", "AUTHENTICATION_EXCEPTION");
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return sendErrorResponse(exchange, "Invalid or expired token", "AUTHENTICATION_EXCEPTION");
        }

        String username = jwtUtil.extractSubject(token);
        ServerWebExchange modifiedExchange = exchange.mutate()
                .request(exchange.getRequest().mutate()
                        .header("X-Auth-Username", username)
                        .build())
                .build();

        // for controllers downstream
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.emptyList()
        );

        return chain.filter(exchange)
                .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
    }

    private Mono<Void> sendErrorResponse(ServerWebExchange exchange, String message, String errorCode) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        String path = exchange.getRequest().getURI().getPath();
        String errorJson = String.format(
                "{\"message\":\"%s\",\"timestamp\":\"%s\",\"path\":\"%s\",\"errorCode\":\"%s\"}",
                message,
                new java.sql.Timestamp(System.currentTimeMillis()),
                path,
                errorCode
        );

        DataBuffer buffer = exchange.getResponse()
                .bufferFactory()
                .wrap(errorJson.getBytes(StandardCharsets.UTF_8));

        return exchange.getResponse().writeWith(Mono.just(buffer));
    }

}

