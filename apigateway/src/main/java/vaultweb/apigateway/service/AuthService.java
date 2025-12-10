package vaultweb.apigateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vaultweb.apigateway.dto.UserDetails;
import vaultweb.apigateway.dto.request.UserRegistrationRequest;
import vaultweb.apigateway.model.User;
import vaultweb.apigateway.repositories.UserRepository;
import vaultweb.apigateway.util.BcryptUtil;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

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
        // create-user and save
        User user = userRepository.save(User.builder()
                .email(request.email())
                .name(request.name())
                .password(BcryptUtil.encode(request.password()))
                .build());
        return UserDetails.builder().email(user.getEmail()).name(user.getName()).build();
    }




}
