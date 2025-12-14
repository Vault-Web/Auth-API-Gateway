package vaultweb.apigateway.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(String accessToken, String refreshToken) {}
