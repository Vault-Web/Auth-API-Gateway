package vaultweb.apigateway.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import vaultweb.apigateway.model.User;

import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, Integer> {
  Mono<Boolean> existsByEmail(String email);

  Mono<Boolean> existsByUsername(String username);

  Mono<User> findByEmailOrUsername(String email, String username);

  Mono<User> findByUsername(String username);
}
