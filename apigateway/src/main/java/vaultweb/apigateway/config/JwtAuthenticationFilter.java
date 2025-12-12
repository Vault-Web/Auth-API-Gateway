package vaultweb.apigateway.config;

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

