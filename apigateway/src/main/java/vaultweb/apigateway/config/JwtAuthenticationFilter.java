package vaultweb.apigateway.config;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import vaultweb.apigateway.util.JwtUtil;

import lombok.NonNull;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter implements WebFilter {
  @Value("${auth.publicUrls}")
  private String[] publicEndpoints;

  private final JwtUtil jwtUtil;
  private final AntPathMatcher pathMatcher = new AntPathMatcher();

  public JwtAuthenticationFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  @NonNull
  public Mono<Void> filter(@NonNull ServerWebExchange exchange, @NonNull WebFilterChain chain) {
    String path = exchange.getRequest().getURI().getPath();
    // Skip JWT validation for public endpoints
    if (Arrays.stream(publicEndpoints).anyMatch(pattern -> pathMatcher.match(pattern, path))) {
      return chain.filter(exchange);
    }
    return authenticateAndFilter(exchange, chain);
  }

  public Mono<Void> authenticateAndFilter(ServerWebExchange exchange, WebFilterChain chain) {
    String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      return sendErrorResponse(exchange, "Missing or invalid Authorization header");
    }

    String token = authHeader.substring(7);

    if (!jwtUtil.validateToken(token)) {
      return sendErrorResponse(exchange, "Invalid or expired token");
    }

    String username = jwtUtil.extractSubject(token);

    Authentication authentication =
        new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

    return chain
        .filter(exchange)
        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication));
  }

  private Mono<Void> sendErrorResponse(ServerWebExchange exchange, String message) {
    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

    String path = exchange.getRequest().getURI().getPath();
    String errorJson =
        String.format(
            "{\"message\":\"%s\",\"timestamp\":\"%s\",\"path\":\"%s\",\"errorCode\":\"%s\"}",
            message,
            new java.sql.Timestamp(System.currentTimeMillis()),
            path,
            "AUTHENTICATION_EXCEPTION");

    DataBuffer buffer =
        exchange.getResponse().bufferFactory().wrap(errorJson.getBytes(StandardCharsets.UTF_8));

    return exchange.getResponse().writeWith(Mono.just(buffer));
  }
}
