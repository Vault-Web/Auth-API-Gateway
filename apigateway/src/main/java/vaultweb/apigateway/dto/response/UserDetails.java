package vaultweb.apigateway.dto.response;

import lombok.Builder;

@Builder
public record UserDetails(String email, String username, String name) {}
