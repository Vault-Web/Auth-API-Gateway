package vaultweb.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vaultweb.apigateway.dto.UserDetails;
import vaultweb.apigateway.dto.request.LoginRequest;
import vaultweb.apigateway.dto.request.UserRegistrationRequest;
import vaultweb.apigateway.dto.response.AuthResponse;
import vaultweb.apigateway.model.RefreshToken;
import vaultweb.apigateway.model.User;
import vaultweb.apigateway.repositories.UserRepository;
import vaultweb.apigateway.util.BcryptUtil;
import vaultweb.apigateway.util.JwtUtil;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;
    private final JwtUtil jwtUtil;

    /**
     * Registers a new user based on the provided registration request.
     *
     * @param request The user registration request containing email, name, and password.
     * @return UserDetails of the newly registered user.
     * @throws RuntimeException if a user with the given email already exists.
     */
    public UserDetails registerUser(UserRegistrationRequest request) {
        // check if user exists by email
        if (userRepository.existsByEmail(request.email()))
            throw new RuntimeException("User with email " + request.email() + " already exists");
        // check username exists
        if (userRepository.existsByUsername(request.username()))
            throw new RuntimeException("User with username " + request.username() + " already exists");
        // create-user and save
        User user = userRepository.save(User.builder()
                .email(request.email())
                .name(request.name())
                .username(request.username())
                .password(BcryptUtil.encode(request.password()))
                .build());
        return UserDetails.builder().email(user.getEmail()).name(user.getName()).username(user.getUsername()).build();
    }

    /**
     * Authenticates a user based on the provided login request.
     *
     * @param request The login request containing email and password.
     * @return AuthResponse containing access and refresh tokens.
     * @throws RuntimeException if the email or password is invalid.
     */
    public AuthResponse login(LoginRequest request) {
        // find user by email
        User user = userRepository.findByEmailOrUsername(request.emailUsername(), request.emailUsername())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
        // check password
        if (!BcryptUtil.matches(request.password(), user.getPassword()))
            throw new RuntimeException("Invalid email or password");
        // gen auth-token
        String accessToken = jwtUtil.generateToken(user.getEmail());
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    // get user-details
    public UserDetails getUserDetails() {
        return UserDetails.builder()
                .email("demo@gmail.com")
                .name("john doe")
                .username("doe-contributor")
                .build();
    }


}
