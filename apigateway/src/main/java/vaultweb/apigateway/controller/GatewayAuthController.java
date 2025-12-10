package vaultweb.apigateway.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vaultweb.apigateway.dto.UserDetails;
import vaultweb.apigateway.dto.request.LoginRequest;
import vaultweb.apigateway.dto.request.UserRegistrationRequest;
import vaultweb.apigateway.dto.response.AuthResponse;
import vaultweb.apigateway.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@ResponseBody
public class GatewayAuthController {
    private final AuthService authService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/me")
    public UserDetails getMyData() {
        return authService.getUserDetails();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public UserDetails register(@Valid @RequestBody UserRegistrationRequest request) {
        return authService.registerUser(request);
    }

    @PostMapping("/logout")
    public String logout() {
        return "logout";
    }

    @PostMapping("change-username")
    public String changeUsername() {
        return "changeUsername";
    }

    @PostMapping("change-email")
    public String changeEmail() {
        return "changeEmail";
    }

    @PostMapping("change-password")
    public String changePassword() {
        return "changePassword";
    }

    @PostMapping("/switch-jwt")
    public String switchJwtToken() {
        return "switchJwtToken";
    }

    @PostMapping("/reset-password")
    public String resetPassword() {
        return "resetPassword";
    }

    @PostMapping("/verify-email")
    public String verifyEmail() {
        return "verifyEmail";
    }
}
