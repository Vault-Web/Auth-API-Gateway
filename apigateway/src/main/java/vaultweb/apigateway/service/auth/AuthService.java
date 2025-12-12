package vaultweb.apigateway.service.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import vaultweb.apigateway.dto.request.LoginRequest;
import vaultweb.apigateway.dto.request.UserRegistrationRequest;
import vaultweb.apigateway.dto.response.AuthResponse;
import vaultweb.apigateway.dto.response.UserDetails;
import vaultweb.apigateway.exceptions.DefaultException;
import vaultweb.apigateway.exceptions.dto.DefaultExceptionLevels;
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
    public Mono<UserDetails> registerUser(UserRegistrationRequest request) {
        return userRepository.existsByEmail(request.email())
                .flatMap(emailExists -> {
                    if (emailExists) {
                        return Mono.error(new DefaultException("User with email " + request.email() + " already exists"));
                    }
                    return userRepository.existsByUsername(request.username());
                })
                .flatMap(usernameExists -> {
                    if (usernameExists) {
                        return Mono.error(new DefaultException("User with username " + request.username() + " already exists"));
                    }
                    return userRepository.save(User.builder()
                            .email(request.email())
                            .name(request.name())
                            .username(request.username())
                            .password(BcryptUtil.encode(request.password()))
                            .build());
                })
                .map(user -> UserDetails.builder()
                        .email(user.getEmail())
                        .name(user.getName())
                        .username(user.getUsername())
                        .build());
    }


    /**
     * Authenticates a user based on the provided login request.
     *
     * @param request The login request containing email and password.
     * @return AuthResponse containing access and refresh tokens.
     * @throws RuntimeException if the email or password is invalid.
     */
    public Mono<AuthResponse> login(LoginRequest request) throws DefaultException {
        // start chain by finding user by email or username
        // find user by email
        return userRepository.findByEmailOrUsername(request.emailUsername(), request.emailUsername()).flatMap(user -> Mono.fromCallable(() -> {
            // check if user is present
            if (user.isEmpty())
                throw new DefaultException("Invalid email or password", DefaultExceptionLevels.AUTHENTICATION_EXCEPTION);
            return user.get();
        })).flatMap(user -> {
            // gen auth-token
            String accessToken = jwtUtil.generateToken(user.getUsername());
            return refreshTokenService.createRefreshToken(user).map(refreshToken -> AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken.getToken())
                    .build());
        });
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
