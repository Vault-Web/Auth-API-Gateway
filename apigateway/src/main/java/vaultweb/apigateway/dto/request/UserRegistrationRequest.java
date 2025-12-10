package vaultweb.apigateway.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record UserRegistrationRequest(
        @Email(message = "a valid email address is required")
        @NotEmpty(message = "email address is required")
        String email,
        @NotEmpty(message = "name is required")
        String name,
        @NotEmpty(message = "password is required")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
                message = "password must be at least 8 characters long and include at least one uppercase letter, one lowercase letter, one digit, and one special character")
        String password
) {
}
