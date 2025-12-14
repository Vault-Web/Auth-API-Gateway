package vaultweb.apigateway.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
    @NotEmpty(message = "Either username or password must be provided") String emailUsername,
    @NotEmpty(message = "Password must not be empty") String password) {}
