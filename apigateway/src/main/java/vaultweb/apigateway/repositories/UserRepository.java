package vaultweb.apigateway.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import vaultweb.apigateway.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Boolean existsByEmail(String email);
}
