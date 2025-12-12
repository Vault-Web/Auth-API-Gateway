package vaultweb.apigateway.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;
import vaultweb.apigateway.model.User;

import java.util.Optional;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsByUsername(String username);
    Mono<Optional<User>> findByEmailOrUsername(String email, String username);
}
